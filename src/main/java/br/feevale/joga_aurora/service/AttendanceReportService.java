package br.feevale.joga_aurora.service;

import br.feevale.joga_aurora.entity.AttendanceEntity;
import br.feevale.joga_aurora.enums.AttendanceStatusEnum;
import br.feevale.joga_aurora.repository.AttendanceRepository;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static br.feevale.joga_aurora.enums.LogStatusEnum.FINISHED;
import static br.feevale.joga_aurora.enums.LogStatusEnum.STARTED;

@Slf4j
@AllArgsConstructor
@Service
public class AttendanceReportService {

    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;

    public byte[] getAttendancesReport(final String id, final Date startDate, final Date endDate) {
        final var start = Instant.now();
        log.info("status={} id={} startDate={} endDate={}", STARTED, id, startDate, endDate);

        final var students = studentRepository.findByClassroom_IdOrderByNameAsc(id);

        final var attendances = attendanceRepository
                .findByStudent_Classroom_IdAndAttendanceDateBetweenOrderByAttendanceDateAsc(id, startDate, endDate);

        final var dates = extractDistinctDates(attendances);

        final Map<String, Map<Date, AttendanceStatusEnum>> attendanceMap = buildAttendanceMap(attendances);

        // Criar planilha
        try (final var workbook = new XSSFWorkbook()) {
            final Sheet sheet = workbook.createSheet("Relatório de Presença");

            // Estilo para cabeçalhos
            final CellStyle headerStyle = workbook.createCellStyle();
            final Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Aplicar cor e espaçamento igual ao StudentReportService
            final byte[] headerColor = new byte[]{(byte) 248, (byte) 203, (byte) 173};
            headerStyle.setFillForegroundColor(new XSSFColor(headerColor, null));
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // Simular padding interno: quebra de linha e indentação
            headerStyle.setWrapText(true);
            headerStyle.setIndention((short) 2);
            setBorders(headerStyle);

            // Estilo padrão para células de dados (nome e status) - igual ao StudentReportService
            final CellStyle normalStyle = workbook.createCellStyle();
            normalStyle.setWrapText(true);
            normalStyle.setIndention((short) 2);
            setBorders(normalStyle);

            final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            createHeaderRow(sheet, headerStyle, dates, dateFormat);

            populateStudentRows(sheet, students, dates, attendanceMap, normalStyle);

            // Ajusta o tamanho das colunas
            autoSizeColumns(sheet, dates.size());

            // Converter para bytes
            try (final var out = new ByteArrayOutputStream()) {
                workbook.write(out);

                log.info("status={} id={} startDate={} endDate={} datesSize={} timeMillis={}", FINISHED, id, startDate, endDate, dates.size(), Duration.between(start, Instant.now()).toMillis());
                return out.toByteArray();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Date> extractDistinctDates(final List<AttendanceEntity> attendances) {
        return attendances.stream()
                .map(AttendanceEntity::getAttendanceDate)
                .distinct()
                .toList();
    }

    private Map<String, Map<Date, AttendanceStatusEnum>> buildAttendanceMap(final List<AttendanceEntity> attendances) {
        final Map<String, Map<Date, AttendanceStatusEnum>> attendanceMap = new HashMap<>();
        attendances.forEach(attendance ->
                attendanceMap
                        .computeIfAbsent(attendance.getStudent().getId(), k -> new HashMap<>())
                        .put(attendance.getAttendanceDate(), attendance.getStatus()));
        return attendanceMap;
    }

    private void createHeaderRow(final Sheet sheet, final CellStyle headerStyle, final List<Date> dates, final SimpleDateFormat dateFormat) {
        final Row headerRow = sheet.createRow(0);
        // Ajuste de altura para simular padding vertical como no StudentReportService
        headerRow.setHeightInPoints(24f);

        final Cell cell0 = headerRow.createCell(0);
        cell0.setCellValue("Estudantes");
        cell0.setCellStyle(headerStyle);

        for (int i = 0; i < dates.size(); i++) {
            final Cell cell = headerRow.createCell(i + 1);
            cell.setCellValue(dateFormat.format(dates.get(i)));
            cell.setCellStyle(headerStyle);
        }
    }

    private void populateStudentRows(final Sheet sheet, final List<?> students, final List<Date> dates, final Map<String, Map<Date, AttendanceStatusEnum>> attendanceMap, final CellStyle normalStyle) {
        int rowIdx = 1;
        for (final var student : students) {
            final Row row = sheet.createRow(rowIdx++);
            // Pequeno aumento de altura para dar padding vertical (simula ~2px)
            row.setHeightInPoints(20f);

            // student is expected to have getName() and getId() as in original code
            final String name;
            final String studentId;
            try {
                name = (String) student.getClass().getMethod("getName").invoke(student);
                studentId = (String) student.getClass().getMethod("getId").invoke(student);
            } catch (Exception e) {
                // Reflection should not fail given original usage; if it does, rethrow as runtime
                throw new RuntimeException(e);
            }

            final Cell nameCell = row.createCell(0);
            nameCell.setCellValue(name);
            nameCell.setCellStyle(normalStyle);

            final Map<Date, AttendanceStatusEnum> studentRecords = attendanceMap.getOrDefault(studentId, Collections.emptyMap());

            for (int i = 0; i < dates.size(); i++) {
                final Date date = dates.get(i);
                final AttendanceStatusEnum status = studentRecords.get(date);

                final String display = Objects.nonNull(status) ? status.getDescription() : AttendanceStatusEnum.ABSENT.getDescription();

                final Cell cell = row.createCell(i + 1);
                cell.setCellValue(display);
                cell.setCellStyle(normalStyle);
            }
        }
    }

    private void autoSizeColumns(final Sheet sheet, final int datesSize) {
        for (int i = 0; i <= datesSize; i++) {
            sheet.autoSizeColumn(i);
        }
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

}
