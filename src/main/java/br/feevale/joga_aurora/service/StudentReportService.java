package br.feevale.joga_aurora.service;

import br.feevale.joga_aurora.entity.BodyMeasurementEntity;
import br.feevale.joga_aurora.entity.PhysicalTestEntity;
import br.feevale.joga_aurora.repository.BodyMeasurementRepository;
import br.feevale.joga_aurora.repository.PhysicalTestRepository;
import br.feevale.joga_aurora.repository.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static br.feevale.joga_aurora.enums.LogStatusEnum.FINISHED;
import static br.feevale.joga_aurora.enums.LogStatusEnum.STARTED;

@Slf4j
@AllArgsConstructor
@Service
public class StudentReportService {

    private final StudentRepository studentRepository;
    private final BodyMeasurementRepository bodyMeasurementRepository;
    private final PhysicalTestRepository physicalTestRepository;

    public byte[] getStudentsReport(final String id, final Date startDate, final Date endDate) {
        final var start = Instant.now();
        log.info("status={} id={} startDate={} endDate={}", STARTED, id, startDate, endDate);

        final var students = studentRepository.findByClassroom_IdOrderByNameAsc(id);

        final var bodyMeasurements = bodyMeasurementRepository
                .findByStudent_Classroom_IdAndCollectionDateBetweenOrderByStudent_NameAscCollectionDateAsc(id, startDate, endDate);

        final var physicalTests = physicalTestRepository
                .findByStudent_Classroom_IdAndCollectionDateBetweenOrderByStudent_NameAscCollectionDateAsc(id, startDate, endDate);

        // Mapear medidas corporais por aluno e data
        final Map<String, Map<Date, BodyMeasurementEntity>> bodyMeasurementMap = new HashMap<>();
        bodyMeasurements.forEach(bm ->
                bodyMeasurementMap
                        .computeIfAbsent(bm.getStudent().getId(), k -> new HashMap<>())
                        .put(bm.getCollectionDate(), bm));

        // Mapear testes físicos por aluno e data
        final Map<String, Map<Date, PhysicalTestEntity>> physicalTestMap = new HashMap<>();
        physicalTests.forEach(pt ->
                physicalTestMap
                        .computeIfAbsent(pt.getStudent().getId(), k -> new HashMap<>())
                        .put(pt.getCollectionDate(), pt));

        // Criar estrutura de dados combinada: aluno + data -> medidas e testes
        final List<StudentDataRow> dataRows = new ArrayList<>();
        students.forEach(student -> {
            final Map<Date, BodyMeasurementEntity> studentBodyMeasurements =
                    bodyMeasurementMap.getOrDefault(student.getId(), new HashMap<>());
            final Map<Date, PhysicalTestEntity> studentPhysicalTests =
                    physicalTestMap.getOrDefault(student.getId(), new HashMap<>());

            // Para cada data que o aluno tenha pelo menos uma medida ou teste
            final var studentDates = new java.util.TreeSet<Date>();
            studentDates.addAll(studentBodyMeasurements.keySet());
            studentDates.addAll(studentPhysicalTests.keySet());

            studentDates.forEach(date ->
                    dataRows.add(new StudentDataRow(
                            student.getId(),
                            student.getName(),
                            student.getBirthDate(),
                            student.getGender().getDescription(),
                            student.getClassroom().getName(),
                            date,
                            studentBodyMeasurements.get(date),
                            studentPhysicalTests.get(date)
                    ))
            );
        });

        // Criar planilha
        try (final var workbook = new XSSFWorkbook()) {
            final Sheet sheet = workbook.createSheet("Relatório de Estudantes");

            // Cor #f8cbad em RGB para cabeçalho
            final byte[] headerColor = new byte[]{(byte) 248, (byte) 203, (byte) 173};

            // Cor #fbd4b4 em RGB para colunas de referência
            final byte[] referenceColor = new byte[]{(byte) 251, (byte) 212, (byte) 180};

            // Cor #FFFF99 em RGB para totais
            final byte[] totalColor = new byte[]{(byte) 255, (byte) 255, (byte) 153};

            // Estilo para cabeçalhos
            final CellStyle headerStyle = workbook.createCellStyle();
            final Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(new XSSFColor(headerColor, null));
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Simular padding interno: quebra de linha e indentação razoável (~2px visual)
            headerStyle.setWrapText(true);
            headerStyle.setIndention((short)2);
            setBorders(headerStyle);

            // Estilo para células de referência (dados)
            final CellStyle referenceStyle = workbook.createCellStyle();
            referenceStyle.setFillForegroundColor(new XSSFColor(referenceColor, null));
            referenceStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            referenceStyle.setWrapText(true);
            referenceStyle.setIndention((short)2);
            setBorders(referenceStyle);

            // Estilo para células de porcentagem
            final CellStyle percentageStyle = workbook.createCellStyle();
            percentageStyle.setFillForegroundColor(new XSSFColor(totalColor, null));
            percentageStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            percentageStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00%"));
            percentageStyle.setWrapText(true);
            percentageStyle.setIndention((short)2);
            setBorders(percentageStyle);

            // Estilo para células de totais
            final CellStyle totalStyle = workbook.createCellStyle();
            totalStyle.setFillForegroundColor(new XSSFColor(totalColor, null));
            totalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            totalStyle.setWrapText(true);
            totalStyle.setIndention((short)2);
            setBorders(totalStyle);

            // Estilo padrão para células normais (sem cor de fundo)
            final CellStyle normalStyle = workbook.createCellStyle();
            normalStyle.setWrapText(true);
            normalStyle.setIndention((short)2);
            setBorders(normalStyle);

            // Cabeçalho
            final Row headerRow = sheet.createRow(0);
            headerRow.setHeightInPoints(24f);
            int colIdx = 0;

            // Colunas básicas
            createHeaderCell(headerRow, colIdx++, "Data de Coleta", headerStyle);
            createHeaderCell(headerRow, colIdx++, "Nome do Estudante", headerStyle);
            createHeaderCell(headerRow, colIdx++, "Data de Nascimento", headerStyle);
            createHeaderCell(headerRow, colIdx++, "Idade", headerStyle);
            createHeaderCell(headerRow, colIdx++, "Gênero", headerStyle);
            createHeaderCell(headerRow, colIdx++, "Turma", headerStyle);

            // Colunas de medidas corporais
            createHeaderCell(headerRow, colIdx++, "Cintura (cm)", headerStyle);
            createHeaderCell(headerRow, colIdx++, "Peso (kg)", headerStyle);
            createHeaderCell(headerRow, colIdx++, "Estatura (cm)", headerStyle);
            createHeaderCell(headerRow, colIdx++, "IMC", headerStyle);
            createHeaderCell(headerRow, colIdx++, "Referência", headerStyle);
            createHeaderCell(headerRow, colIdx++, "Relação Cintura/Estatura", headerStyle);
            createHeaderCell(headerRow, colIdx++, "Referência", headerStyle);

            // Colunas de testes físicos
            createHeaderCell(headerRow, colIdx++, "Teste 6 Minutos", headerStyle);
            createHeaderCell(headerRow, colIdx++, "Referência", headerStyle);
            createHeaderCell(headerRow, colIdx++, "Teste Flex", headerStyle);
            createHeaderCell(headerRow, colIdx++, "Referência", headerStyle);
            createHeaderCell(headerRow, colIdx++, "Teste RML", headerStyle);
            createHeaderCell(headerRow, colIdx++, "Referência", headerStyle);
            createHeaderCell(headerRow, colIdx++, "Teste 20 Metros", headerStyle);
            createHeaderCell(headerRow, colIdx++, "Referência", headerStyle);
            createHeaderCell(headerRow, colIdx++, "Teste Arremesso 2kg", headerStyle);
            createHeaderCell(headerRow, colIdx++, "Referência", headerStyle);

            // Linhas de dados
            int rowIdx = 1;
            final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            for (final var dataRow : dataRows) {
                final Row row = sheet.createRow(rowIdx++);
                // Pequeno aumento de altura para dar padding vertical (simula ~2px)
                row.setHeightInPoints(20f);
                int cellIdx = 0;

                // Data de Coleta primeiro
                final Cell dateCell = row.createCell(cellIdx++);
                dateCell.setCellValue(dateFormat.format(dataRow.collectionDate));
                dateCell.setCellStyle(normalStyle);

                // Dados do estudante
                final Cell nameCell = row.createCell(cellIdx++);
                nameCell.setCellValue(dataRow.studentName);
                nameCell.setCellStyle(normalStyle);

                final Cell birthDateCell = row.createCell(cellIdx++);
                birthDateCell.setCellValue(dateFormat.format(dataRow.birthDate));
                birthDateCell.setCellStyle(normalStyle);

                // Calcular idade
                final LocalDate birthLocalDate = dataRow.birthDate.toLocalDate();
                final LocalDate collectionLocalDate = dataRow.collectionDate.toLocalDate();
                final int age = Period.between(birthLocalDate, collectionLocalDate).getYears();

                final Cell ageCell = row.createCell(cellIdx++);
                ageCell.setCellValue(age);
                ageCell.setCellStyle(normalStyle);

                final Cell genderCell = row.createCell(cellIdx++);
                genderCell.setCellValue(dataRow.gender);
                genderCell.setCellStyle(normalStyle);

                final Cell classroomCell = row.createCell(cellIdx++);
                classroomCell.setCellValue(dataRow.classroomName);
                classroomCell.setCellStyle(normalStyle);

                // Medidas corporais
                final var bm = dataRow.bodyMeasurement;
                if (Objects.nonNull(bm)) {
                    final Cell waistCell = row.createCell(cellIdx++);
                    waistCell.setCellValue(bm.getWaist());
                    waistCell.setCellStyle(normalStyle);

                    final Cell weightCell = row.createCell(cellIdx++);
                    weightCell.setCellValue(bm.getWeight());
                    weightCell.setCellStyle(normalStyle);

                    final Cell heightCell = row.createCell(cellIdx++);
                    heightCell.setCellValue(bm.getHeight());
                    heightCell.setCellStyle(normalStyle);

                    final Cell bmiCell = row.createCell(cellIdx++);
                    bmiCell.setCellValue(bm.getBmi());
                    bmiCell.setCellStyle(normalStyle);

                    final Cell bmiRefCell = row.createCell(cellIdx++);
                    bmiRefCell.setCellValue(bm.getBmiReference().getDescription());
                    bmiRefCell.setCellStyle(referenceStyle);

                    final Cell waistHeightRatioCell = row.createCell(cellIdx++);
                    waistHeightRatioCell.setCellValue(bm.getWaistHeightRatio());
                    waistHeightRatioCell.setCellStyle(normalStyle);

                    final Cell waistHeightRefCell = row.createCell(cellIdx++);
                    waistHeightRefCell.setCellValue(bm.getWaistHeightRatioReference().getDescription());
                    waistHeightRefCell.setCellStyle(referenceStyle);
                } else {
                    // Criar células vazias com estilo nas colunas de referência
                    final Cell waistEmptyCell = row.createCell(cellIdx++);
                    waistEmptyCell.setCellStyle(normalStyle);

                    final Cell weightEmptyCell = row.createCell(cellIdx++);
                    weightEmptyCell.setCellStyle(normalStyle);

                    final Cell heightEmptyCell = row.createCell(cellIdx++);
                    heightEmptyCell.setCellStyle(normalStyle);

                    final Cell bmiEmptyCell = row.createCell(cellIdx++);
                    bmiEmptyCell.setCellStyle(normalStyle);

                    final Cell bmiRefEmptyCell = row.createCell(cellIdx++);
                    bmiRefEmptyCell.setCellStyle(referenceStyle); // Referência IMC vazia

                    final Cell waistHeightRatioEmptyCell = row.createCell(cellIdx++);
                    waistHeightRatioEmptyCell.setCellStyle(normalStyle);

                    final Cell waistHeightRefEmptyCell = row.createCell(cellIdx++);
                    waistHeightRefEmptyCell.setCellStyle(referenceStyle); // Referência Cintura/Estatura vazia
                }

                // Testes físicos
                final var pt = dataRow.physicalTest;
                if (Objects.nonNull(pt)) {
                    final Cell sixMinutesCell = row.createCell(cellIdx++);
                    sixMinutesCell.setCellValue(pt.getSixMinutesTest());
                    sixMinutesCell.setCellStyle(normalStyle);

                    final Cell sixMinRefCell = row.createCell(cellIdx++);
                    sixMinRefCell.setCellValue(pt.getSixMinutesReference().getDescription());
                    sixMinRefCell.setCellStyle(referenceStyle);

                    final Cell flexCell = row.createCell(cellIdx++);
                    flexCell.setCellValue(pt.getFlexTest());
                    flexCell.setCellStyle(normalStyle);

                    final Cell flexRefCell = row.createCell(cellIdx++);
                    flexRefCell.setCellValue(pt.getFlexReference().getDescription());
                    flexRefCell.setCellStyle(referenceStyle);

                    final Cell rmlCell = row.createCell(cellIdx++);
                    rmlCell.setCellValue(pt.getRmlTest());
                    rmlCell.setCellStyle(normalStyle);

                    final Cell rmlRefCell = row.createCell(cellIdx++);
                    rmlRefCell.setCellValue(pt.getRmlReference().getDescription());
                    rmlRefCell.setCellStyle(referenceStyle);

                    final Cell twentyMetersCell = row.createCell(cellIdx++);
                    twentyMetersCell.setCellValue(pt.getTwentyMetersTest());
                    twentyMetersCell.setCellStyle(normalStyle);

                    final Cell twentyMetersRefCell = row.createCell(cellIdx++);
                    twentyMetersRefCell.setCellValue(pt.getTwentyMetersReference().getDescription());
                    twentyMetersRefCell.setCellStyle(referenceStyle);

                    final Cell throwTwoKgCell = row.createCell(cellIdx++);
                    throwTwoKgCell.setCellValue(pt.getThrowTwoKgTest());
                    throwTwoKgCell.setCellStyle(normalStyle);

                    final Cell throwRefCell = row.createCell(cellIdx++);
                    throwRefCell.setCellValue(pt.getThrowTwoKgReference().getDescription());
                    throwRefCell.setCellStyle(referenceStyle);
                } else {
                    // Criar células vazias com estilo nas colunas de referência
                    final Cell sixMinutesEmptyCell = row.createCell(cellIdx++);
                    sixMinutesEmptyCell.setCellStyle(normalStyle);

                    final Cell sixMinRefEmptyCell = row.createCell(cellIdx++);
                    sixMinRefEmptyCell.setCellStyle(referenceStyle); // Referência 6 Minutos vazia

                    final Cell flexEmptyCell = row.createCell(cellIdx++);
                    flexEmptyCell.setCellStyle(normalStyle);

                    final Cell flexRefEmptyCell = row.createCell(cellIdx++);
                    flexRefEmptyCell.setCellStyle(referenceStyle); // Referência Flex vazia

                    final Cell rmlEmptyCell = row.createCell(cellIdx++);
                    rmlEmptyCell.setCellStyle(normalStyle);

                    final Cell rmlRefEmptyCell = row.createCell(cellIdx++);
                    rmlRefEmptyCell.setCellStyle(referenceStyle); // Referência RML vazia

                    final Cell twentyMetersEmptyCell = row.createCell(cellIdx++);
                    twentyMetersEmptyCell.setCellStyle(normalStyle);

                    final Cell twentyMetersRefEmptyCell = row.createCell(cellIdx++);
                    twentyMetersRefEmptyCell.setCellStyle(referenceStyle); // Referência 20 Metros vazia

                    final Cell throwTwoKgEmptyCell = row.createCell(cellIdx++);
                    throwTwoKgEmptyCell.setCellStyle(normalStyle);

                    final Cell throwRefEmptyCell = row.createCell(cellIdx++);
                    throwRefEmptyCell.setCellStyle(referenceStyle); // Referência Arremesso 2kg vazia
                }
            }

            // Calcular quantidade de estudantes distintos (contagem por ID do estudante)
            final int distinctStudents = (int) dataRows.stream()
                    .map(StudentDataRow::studentId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .count();

            // Adicionar linhas de resumo/estatísticas
            addSummaryRows(sheet, rowIdx, dataRows.size(), distinctStudents, headerStyle, referenceStyle, percentageStyle, totalStyle);

            // Ajusta o tamanho das colunas
            for (int i = 0; i < colIdx; i++) {
                sheet.autoSizeColumn(i);
            }

            // Converter para bytes
            try (final var out = new ByteArrayOutputStream()) {
                workbook.write(out);

                log.info("status={} id={} startDate={} endDate={} rowsCount={} timeMillis={}",
                        FINISHED, id, startDate, endDate, dataRows.size(), Duration.between(start, Instant.now()).toMillis());
                return out.toByteArray();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createHeaderCell(final Row row, final int colIdx, final String value, final CellStyle style) {
        final Cell cell = row.createCell(colIdx);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void setBorders(final CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        // Centralizar verticalmente para melhorar o 'padding' visual
        style.setVerticalAlignment(VerticalAlignment.CENTER);
    }

    private void addSummaryRows(final Sheet sheet, final int startRowIdx, final int dataRowCount,
                                final int distinctStudentCount,
                                final CellStyle headerStyle, final CellStyle referenceStyle,
                                final CellStyle percentageStyle, final CellStyle totalStyle) {
        int currentRow = startRowIdx;

        // Criar estilo para células vazias (com bordas)
        final CellStyle normalStyle = sheet.getWorkbook().createCellStyle();
        setBorders(normalStyle);

        // Índices das colunas de referência (baseado em zero, após nome, data nascimento, idade, gênero, turma e data coleta)
        // Coluna 10: Referência IMC
        // Coluna 12: Referência Cintura/Estatura
        // Coluna 14: Referência 6 Minutos
        // Coluna 16: Referência Flex
        // Coluna 18: Referência RML
        // Coluna 20: Referência 20 Metros
        // Coluna 22: Referência Arremesso 2kg
        final int[] referenceColumns = {10, 12, 14, 16, 18, 20, 22};

        // Adicionar 2 linhas vazias para separação com bordas
        final int totalColumns = 23; // Total de colunas na planilha (adicionadas 4 novas colunas de informações do aluno)
        for (int emptyRowCount = 0; emptyRowCount < 2; emptyRowCount++) {
            final Row emptyRow = sheet.createRow(currentRow++);
            for (int col = 0; col < totalColumns; col++) {
                final Cell emptyCell = emptyRow.createCell(col);

                final int finalCol = col;
                if (Arrays.stream(referenceColumns).anyMatch(refCol -> refCol == finalCol)) {
                    emptyCell.setCellStyle(referenceStyle);
                } else {
                    emptyCell.setCellStyle(normalStyle);
                }
            }
        }

        // Salvar a linha inicial das estatísticas
        final int statsStartRow = currentRow;

        // Criar linhas de resumo: Total, C/Risco, S/Risco, %C/Risco, %S/Risco
        final String[] summaryLabels = {"Total", "C/Risco", "S/Risco", "%C/Risco", "%S/Risco"};

        for (int i = 0; i < summaryLabels.length; i++) {
            final Row summaryRow = sheet.createRow(currentRow++);

            // Para cada coluna de referência, adicionar o label e a estatística
            for (int j = 0; j < referenceColumns.length; j++) {
                final int colIdx = referenceColumns[j];

                // Criar célula com label à esquerda (coluna anterior)
                final Cell labelCell = summaryRow.createCell(colIdx - 1);
                labelCell.setCellValue(summaryLabels[i]);
                labelCell.setCellStyle(headerStyle);

                // Criar célula com o valor/fórmula
                final Cell cell = summaryRow.createCell(colIdx);

                // Aplicar estilo apropriado dependendo da linha
                if (i == 3 || i == 4) {
                    // Linhas de porcentagem
                    cell.setCellStyle(percentageStyle);
                } else {
                    cell.setCellStyle(totalStyle);
                }

                // Faixa de dados (linha 2 até a última linha de dados)
                final int firstDataRow = 2; // Row 1 é o cabeçalho, dados começam em 2
                final int lastDataRow = startRowIdx; // Última linha de dados
                final String columnLetter = getColumnLetter(colIdx);
                final String range = columnLetter + firstDataRow + ":" + columnLetter + lastDataRow;

                switch (i) {
                    case 0: // Total - contar células não vazias
                        cell.setCellFormula("COUNTIF(" + range + ",\"<>\")");
                        break;
                    case 1: // C/Risco - contar "Com Risco"
                        cell.setCellFormula("COUNTIF(" + range + ",\"Com Risco\")");
                        break;
                    case 2: // S/Risco - contar "Sem Risco"
                        cell.setCellFormula("COUNTIF(" + range + ",\"Sem Risco\")");
                        break;
                    case 3: // %C/Risco - percentual com risco
                        final String totalCell = columnLetter + (statsStartRow + 1); // Linha do Total
                        final String cRiscoCell = columnLetter + (statsStartRow + 2); // Linha do C/Risco
                        cell.setCellFormula("IF(" + totalCell + "=0,0," + cRiscoCell + "/" + totalCell + ")");
                        break;
                    case 4: // %S/Risco - percentual sem risco
                        final String totalCell2 = columnLetter + (statsStartRow + 1); // Linha do Total
                        final String sRiscoCell = columnLetter + (statsStartRow + 3); // Linha do S/Risco
                        cell.setCellFormula("IF(" + totalCell2 + "=0,0," + sRiscoCell + "/" + totalCell2 + ")");
                        break;
                }
            }
        }

        // Adicionar "Total de avaliados" na primeira linha de estatísticas
        final Row totalStudentsRow = sheet.getRow(statsStartRow);
        final Cell totalStudentsLabelCell = totalStudentsRow.createCell(1);
        totalStudentsLabelCell.setCellValue("Total de avaliados");
        totalStudentsLabelCell.setCellStyle(headerStyle);

        final Cell totalStudentsValueCell = totalStudentsRow.createCell(2);
        totalStudentsValueCell.setCellValue(distinctStudentCount);
        totalStudentsValueCell.setCellStyle(totalStyle);
    }

    private String getColumnLetter(final int columnIndex) {
        final StringBuilder columnLetter = new StringBuilder();
        int index = columnIndex;

        while (index >= 0) {
            columnLetter.insert(0, (char) ('A' + (index % 26)));
            index = (index / 26) - 1;
        }

        return columnLetter.toString();
    }

    // Classe interna para facilitar a organização dos dados
    private record StudentDataRow(
            String studentId,
            String studentName,
            Date birthDate,
            String gender,
            String classroomName,
            Date collectionDate,
            BodyMeasurementEntity bodyMeasurement,
            PhysicalTestEntity physicalTest
    ) {
    }

}