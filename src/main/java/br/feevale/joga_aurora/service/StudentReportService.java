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
import java.time.Period;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Stream;

import static br.feevale.joga_aurora.enums.LogStatusEnum.FINISHED;
import static br.feevale.joga_aurora.enums.LogStatusEnum.STARTED;

/**
 * Serviço responsável pela geração de relatórios de estudantes em formato XLSX.
 * Aplica princípios SOLID e Clean Code para manutenibilidade e clareza.
 */
@Slf4j
@AllArgsConstructor
@Service
public class StudentReportService {

    // === CONSTANTES ===
    private static final int HEADER_ROW_HEIGHT = 25;
    private static final int DATA_ROW_HEIGHT = 20;
    private static final int EMPTY_ROW_HEIGHT = 20;
    private static final int SUMMARY_ROW_HEIGHT = 22;
    private static final double COLUMN_WIDTH_MULTIPLIER = 1.2;
    private static final int TOTAL_COLUMNS = 23;
    private static final int FIRST_DATA_ROW = 2;
    private static final String DATE_FORMAT_PATTERN = "dd/MM/yyyy";
    private static final String PERCENTAGE_FORMAT = "0.00%";
    private static final String SHEET_NAME = "Relatório de Estudantes";

    private static final int[] REFERENCE_COLUMNS = {10, 12, 14, 16, 18, 20, 22};

    private static final byte[] HEADER_COLOR = {(byte) 248, (byte) 203, (byte) 173};
    private static final byte[] REFERENCE_COLOR = {(byte) 251, (byte) 212, (byte) 180};
    private static final byte[] TOTAL_COLOR = {(byte) 255, (byte) 255, (byte) 153};

    // === DEPENDÊNCIAS ===
    private final StudentRepository studentRepository;
    private final BodyMeasurementRepository bodyMeasurementRepository;
    private final PhysicalTestRepository physicalTestRepository;

    // === MÉTODO PÚBLICO ===

    /**
     * Gera relatório completo de estudantes em formato XLSX.
     *
     * @param classroomId ID da turma
     * @param startDate Data inicial do período
     * @param endDate Data final do período
     * @return Array de bytes do arquivo XLSX gerado
     */
    public byte[] getStudentsReport(final String classroomId, final Date startDate, final Date endDate) {
        final var start = Instant.now();
        log.info("status={} id={} startDate={} endDate={}", STARTED, classroomId, startDate, endDate);

        final List<StudentDataRow> dataRows = aggregateStudentData(classroomId, startDate, endDate);
        final byte[] report = generateWorkbook(dataRows);

        log.info("status={} id={} startDate={} endDate={} rowsCount={} timeMillis={}",
                FINISHED, classroomId, startDate, endDate, dataRows.size(),
                Duration.between(start, Instant.now()).toMillis());

        return report;
    }

    // === AGREGAÇÃO DE DADOS ===

    private List<StudentDataRow> aggregateStudentData(final String classroomId,
                                                       final Date startDate,
                                                       final Date endDate) {
        final var students = studentRepository.findByClassroom_IdOrderByNameAsc(classroomId);
        final var bodyMeasurementMap = buildBodyMeasurementMap(classroomId, startDate, endDate);
        final var physicalTestMap = buildPhysicalTestMap(classroomId, startDate, endDate);

        return students.stream()
                .flatMap(student -> buildStudentDataRows(student, bodyMeasurementMap, physicalTestMap))
                .toList();
    }

    private Map<String, Map<Date, BodyMeasurementEntity>> buildBodyMeasurementMap(
            final String classroomId, final Date startDate, final Date endDate) {
        final var measurements = bodyMeasurementRepository
                .findByStudent_Classroom_IdAndCollectionDateBetweenOrderByStudent_NameAscCollectionDateAsc(
                        classroomId, startDate, endDate);

        return measurements.stream()
                .collect(HashMap::new,
                        (map, bm) -> map.computeIfAbsent(bm.getStudent().getId(), k -> new HashMap<>())
                                .put(bm.getCollectionDate(), bm),
                        HashMap::putAll);
    }

    private Map<String, Map<Date, PhysicalTestEntity>> buildPhysicalTestMap(
            final String classroomId, final Date startDate, final Date endDate) {
        final var tests = physicalTestRepository
                .findByStudent_Classroom_IdAndCollectionDateBetweenOrderByStudent_NameAscCollectionDateAsc(
                        classroomId, startDate, endDate);

        return tests.stream()
                .collect(HashMap::new,
                        (map, pt) -> map.computeIfAbsent(pt.getStudent().getId(), k -> new HashMap<>())
                                .put(pt.getCollectionDate(), pt),
                        HashMap::putAll);
    }

    private Stream<StudentDataRow> buildStudentDataRows(
            final br.feevale.joga_aurora.entity.StudentEntity student,
            final Map<String, Map<Date, BodyMeasurementEntity>> bodyMeasurementMap,
            final Map<String, Map<Date, PhysicalTestEntity>> physicalTestMap) {

        final var studentBodyMeasurements = bodyMeasurementMap.getOrDefault(student.getId(), Map.of());
        final var studentPhysicalTests = physicalTestMap.getOrDefault(student.getId(), Map.of());

        final var allDates = new TreeSet<Date>();
        allDates.addAll(studentBodyMeasurements.keySet());
        allDates.addAll(studentPhysicalTests.keySet());

        return allDates.stream()
                .map(date -> new StudentDataRow(
                        student.getName(),
                        student.getBirthDate(),
                        student.getGender().getDescription(),
                        student.getClassroom().getName(),
                        date,
                        studentBodyMeasurements.get(date),
                        studentPhysicalTests.get(date)
                ));
    }

    // === GERAÇÃO DO WORKBOOK ===

    private byte[] generateWorkbook(final List<StudentDataRow> dataRows) {
        try (final var workbook = new XSSFWorkbook()) {
            final Sheet sheet = workbook.createSheet(SHEET_NAME);
            final var styles = createCellStyles(workbook);
            final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);

            final int columnCount = createHeaderRow(sheet, styles.headerStyle());
            int rowIndex = createDataRows(sheet, dataRows, dateFormat, styles);
            addSummarySection(sheet, rowIndex, dataRows.size(), styles);
            adjustColumnWidths(sheet, columnCount);

            return convertWorkbookToBytes(workbook);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar relatório de estudantes", e);
        }
    }

    // === ESTILOS ===

    private CellStyles createCellStyles(final XSSFWorkbook workbook) {
        return new CellStyles(
                createHeaderStyle(workbook),
                createColoredStyle(workbook, REFERENCE_COLOR),
                createColoredStyle(workbook, TOTAL_COLOR),
                createPercentageStyle(workbook),
                createNormalStyle(workbook)
        );
    }

    private CellStyle createHeaderStyle(final XSSFWorkbook workbook) {
        final CellStyle style = workbook.createCellStyle();
        final Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(new XSSFColor(HEADER_COLOR, null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        applyBordersAndAlignment(style);
        return style;
    }

    private CellStyle createColoredStyle(final XSSFWorkbook workbook, final byte[] color) {
        final CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(new XSSFColor(color, null));
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        applyBordersAndAlignment(style);
        return style;
    }

    private CellStyle createPercentageStyle(final XSSFWorkbook workbook) {
        final CellStyle style = createColoredStyle(workbook, TOTAL_COLOR);
        style.setDataFormat(workbook.createDataFormat().getFormat(PERCENTAGE_FORMAT));
        return style;
    }

    private CellStyle createNormalStyle(final XSSFWorkbook workbook) {
        final CellStyle style = workbook.createCellStyle();
        applyBordersAndAlignment(style);
        return style;
    }

    private void applyBordersAndAlignment(final CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
    }

    // === CABEÇALHO ===

    private int createHeaderRow(final Sheet sheet, final CellStyle headerStyle) {
        final Row headerRow = sheet.createRow(0);
        headerRow.setHeightInPoints(HEADER_ROW_HEIGHT);

        final String[] headers = {
                "Data de Coleta", "Nome do Estudante", "Data de Nascimento", "Idade", "Gênero", "Turma",
                "Cintura (cm)", "Peso (kg)", "Estatura (cm)", "IMC", "Referência",
                "Relação Cintura/Estatura", "Referência",
                "Teste 6 Minutos", "Referência", "Teste Flex", "Referência",
                "Teste RML", "Referência", "Teste 20 Metros", "Referência",
                "Teste Arremesso 2kg", "Referência"
        };

        for (int i = 0; i < headers.length; i++) {
            createCell(headerRow, i, headers[i], headerStyle);
        }

        return headers.length;
    }

    // === LINHAS DE DADOS ===

    private int createDataRows(final Sheet sheet, final List<StudentDataRow> dataRows,
                                final SimpleDateFormat dateFormat, final CellStyles styles) {
        int rowIndex = 1;
        for (final var dataRow : dataRows) {
            createDataRow(sheet, rowIndex++, dataRow, dateFormat, styles);
        }
        return rowIndex;
    }

    private void createDataRow(final Sheet sheet, final int rowIndex, final StudentDataRow dataRow,
                                final SimpleDateFormat dateFormat, final CellStyles styles) {
        final Row row = sheet.createRow(rowIndex);
        row.setHeightInPoints(DATA_ROW_HEIGHT);

        int cellIndex = 0;
        cellIndex = populateStudentInfo(row, cellIndex, dataRow, dateFormat, styles.normalStyle());
        cellIndex = populateBodyMeasurements(row, cellIndex, dataRow.bodyMeasurement(), styles);
        populatePhysicalTests(row, cellIndex, dataRow.physicalTest(), styles);
    }

    private int populateStudentInfo(final Row row, int cellIndex, final StudentDataRow dataRow,
                                     final SimpleDateFormat dateFormat, final CellStyle style) {
        createCell(row, cellIndex++, dateFormat.format(dataRow.collectionDate()), style);
        createCell(row, cellIndex++, dataRow.studentName(), style);
        createCell(row, cellIndex++, dateFormat.format(dataRow.birthDate()), style);
        createCell(row, cellIndex++, calculateAge(dataRow.birthDate(), dataRow.collectionDate()), style);
        createCell(row, cellIndex++, dataRow.gender(), style);
        createCell(row, cellIndex++, dataRow.classroomName(), style);
        return cellIndex;
    }

    private int populateBodyMeasurements(final Row row, int cellIndex, final BodyMeasurementEntity bm,
                                          final CellStyles styles) {
        if (Objects.nonNull(bm)) {
            createCell(row, cellIndex++, bm.getWaist(), styles.normalStyle());
            createCell(row, cellIndex++, bm.getWeight(), styles.normalStyle());
            createCell(row, cellIndex++, bm.getHeight(), styles.normalStyle());
            createCell(row, cellIndex++, bm.getBmi(), styles.normalStyle());
            createCell(row, cellIndex++, bm.getBmiReference().getDescription(), styles.referenceStyle());
            createCell(row, cellIndex++, bm.getWaistHeightRatio(), styles.normalStyle());
            createCell(row, cellIndex++, bm.getWaistHeightRatioReference().getDescription(), styles.referenceStyle());
        } else {
            cellIndex = createEmptyCells(row, cellIndex, styles.normalStyle(), 4);
            createCell(row, cellIndex++, "", styles.referenceStyle());
            createCell(row, cellIndex++, "", styles.normalStyle());
            createCell(row, cellIndex++, "", styles.referenceStyle());
        }
        return cellIndex;
    }

    private int populatePhysicalTests(final Row row, int cellIndex, final PhysicalTestEntity pt,
                                       final CellStyles styles) {
        if (Objects.nonNull(pt)) {
            createCell(row, cellIndex++, pt.getSixMinutesTest(), styles.normalStyle());
            createCell(row, cellIndex++, pt.getSixMinutesReference().getDescription(), styles.referenceStyle());
            createCell(row, cellIndex++, pt.getFlexTest(), styles.normalStyle());
            createCell(row, cellIndex++, pt.getFlexReference().getDescription(), styles.referenceStyle());
            createCell(row, cellIndex++, pt.getRmlTest(), styles.normalStyle());
            createCell(row, cellIndex++, pt.getRmlReference().getDescription(), styles.referenceStyle());
            createCell(row, cellIndex++, pt.getTwentyMetersTest(), styles.normalStyle());
            createCell(row, cellIndex++, pt.getTwentyMetersReference().getDescription(), styles.referenceStyle());
            createCell(row, cellIndex++, pt.getThrowTwoKgTest(), styles.normalStyle());
            createCell(row, cellIndex++, pt.getThrowTwoKgReference().getDescription(), styles.referenceStyle());
        } else {
            for (int i = 0; i < 5; i++) {
                createCell(row, cellIndex++, "", styles.normalStyle());
                createCell(row, cellIndex++, "", styles.referenceStyle());
            }
        }
        return cellIndex;
    }

    //=== SEÇÃO DE RESUMO ===

    private void addSummarySection(final Sheet sheet, final int startRowIdx,
                                    final int dataRowCount, final CellStyles styles) {
        int currentRow = createEmptyRows(sheet, startRowIdx, styles);
        final int statsStartRow = currentRow;

        createStatisticsRows(sheet, currentRow, startRowIdx, statsStartRow, styles);
        addTotalStudentsInfo(sheet, statsStartRow, dataRowCount, styles);
    }

    private int createEmptyRows(final Sheet sheet, int startRow, final CellStyles styles) {
        for (int i = 0; i < 2; i++) {
            final Row emptyRow = sheet.createRow(startRow++);
            emptyRow.setHeightInPoints(EMPTY_ROW_HEIGHT);

            for (int col = 0; col < TOTAL_COLUMNS; col++) {
                final CellStyle style = isReferenceColumn(col) ? styles.referenceStyle() : styles.normalStyle();
                createCell(emptyRow, col, "", style);
            }
        }
        return startRow;
    }

    private void createStatisticsRows(final Sheet sheet, int currentRow, final int dataEndRow,
                                       final int statsStartRow, final CellStyles styles) {
        final String[] summaryLabels = {"Total", "C/Risco", "S/Risco", "%C/Risco", "%S/Risco"};

        for (int i = 0; i < summaryLabels.length; i++) {
            final Row summaryRow = sheet.createRow(currentRow++);
            summaryRow.setHeightInPoints(SUMMARY_ROW_HEIGHT);

            for (final int refColumn : REFERENCE_COLUMNS) {
                createStatisticCell(summaryRow, refColumn, i, summaryLabels[i],
                                  dataEndRow, statsStartRow, styles);
            }
        }
    }

    private void createStatisticCell(final Row row, final int refColumn, final int statisticIndex,
                                      final String label, final int dataEndRow, final int statsStartRow,
                                      final CellStyles styles) {
        createCell(row, refColumn - 1, label, styles.headerStyle());

        final Cell cell = row.createCell(refColumn);
        final boolean isPercentage = statisticIndex == 3 || statisticIndex == 4;
        cell.setCellStyle(isPercentage ? styles.percentageStyle() : styles.totalStyle());

        final String formula = buildStatisticFormula(refColumn, statisticIndex, dataEndRow, statsStartRow);
        cell.setCellFormula(formula);
    }

    private String buildStatisticFormula(final int column, final int statisticIndex,
                                          final int dataEndRow, final int statsStartRow) {
        final String columnLetter = getColumnLetter(column);
        final String range = columnLetter + FIRST_DATA_ROW + ":" + columnLetter + dataEndRow;

        return switch (statisticIndex) {
            case 0 -> "COUNTA(" + range + ")";
            case 1 -> "COUNTIF(" + range + ",\"Com Risco\")";
            case 2 -> "COUNTIF(" + range + ",\"Sem Risco\")";
            case 3 -> {
                final String total = columnLetter + (statsStartRow + 1);
                final String comRisco = columnLetter + (statsStartRow + 2);
                yield "IF(" + total + "=0,0," + comRisco + "/" + total + ")";
            }
            case 4 -> {
                final String total = columnLetter + (statsStartRow + 1);
                final String semRisco = columnLetter + (statsStartRow + 3);
                yield "IF(" + total + "=0,0," + semRisco + "/" + total + ")";
            }
            default -> "";
        };
    }

    private void addTotalStudentsInfo(final Sheet sheet, final int statsStartRow,
                                       final int dataRowCount, final CellStyles styles) {
        final Row totalRow = sheet.getRow(statsStartRow);
        createCell(totalRow, 1, "Total de avaliados", styles.headerStyle());
        createCell(totalRow, 2, dataRowCount, styles.totalStyle());
    }

    // === UTILITÁRIOS ===

    private void createCell(final Row row, final int colIndex, final String value, final CellStyle style) {
        final Cell cell = row.createCell(colIndex);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void createCell(final Row row, final int colIndex, final Number value, final CellStyle style) {
        final Cell cell = row.createCell(colIndex);
        cell.setCellValue(value.doubleValue());
        cell.setCellStyle(style);
    }

    private int createEmptyCells(final Row row, int startIndex, final CellStyle style, final int count) {
        for (int i = 0; i < count; i++) {
            createCell(row, startIndex++, "", style);
        }
        return startIndex;
    }

    private int calculateAge(final Date birthDate, final Date collectionDate) {
        return Period.between(birthDate.toLocalDate(), collectionDate.toLocalDate()).getYears();
    }

    private boolean isReferenceColumn(final int columnIndex) {
        return Arrays.stream(REFERENCE_COLUMNS).anyMatch(refCol -> refCol == columnIndex);
    }

    private void adjustColumnWidths(final Sheet sheet, final int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
            final int currentWidth = sheet.getColumnWidth(i);
            sheet.setColumnWidth(i, (int) (currentWidth * COLUMN_WIDTH_MULTIPLIER));
        }
    }

    private byte[] convertWorkbookToBytes(final XSSFWorkbook workbook) throws IOException {
        try (final var out = new ByteArrayOutputStream()) {
            workbook.write(out);
            return out.toByteArray();
        }
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

    // === RECORDS ===

    private record StudentDataRow(
            String studentName,
            Date birthDate,
            String gender,
            String classroomName,
            Date collectionDate,
            BodyMeasurementEntity bodyMeasurement,
            PhysicalTestEntity physicalTest
    ) {}

    private record CellStyles(
            CellStyle headerStyle,
            CellStyle referenceStyle,
            CellStyle totalStyle,
            CellStyle percentageStyle,
            CellStyle normalStyle
    ) {}
}

