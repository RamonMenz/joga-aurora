package br.feevale.joga_aurora.service;

import br.feevale.joga_aurora.entity.AttendanceEntity;
import br.feevale.joga_aurora.enums.AttendanceStatusEnum;
import br.feevale.joga_aurora.repository.AttendanceRepository;
import br.feevale.joga_aurora.repository.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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
import java.util.Map;
import java.util.Objects;

import static br.feevale.joga_aurora.enums.LogStatusEnum.FINISHED;
import static br.feevale.joga_aurora.enums.LogStatusEnum.STARTED;

@Slf4j
@AllArgsConstructor
@Service
public class ClassroomAttendanceReportService {

    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;

    public byte[] getAttendancesReport(final String id, final Date startDate, final Date endDate) {
        final var start = Instant.now();
        log.info("status={} id={} startDate={} endDate={}", STARTED, id, startDate, endDate);

        final var students = studentRepository
                .findByClassroom_IdOrderByNameAsc(id);

        final var attendances = attendanceRepository
                .findByStudent_Classroom_IdAndAttendanceDateBetweenOrderByAttendanceDateAsc(id, startDate, endDate);

        final var dates = attendances.stream()
                .map(AttendanceEntity::getAttendanceDate)
                .distinct()
                .toList();

        final Map<String, Map<Date, AttendanceStatusEnum>> attendanceMap = new HashMap<>();
        attendances.forEach(attendance ->
                attendanceMap
                        .computeIfAbsent(attendance.getStudent().getId(), k -> new HashMap<>())
                        .put(attendance.getAttendanceDate(), attendance.getStatus()));

        // Criar planilha
        try (final var workbook = new XSSFWorkbook()) {
            final Sheet sheet = workbook.createSheet("Relatório de Presença");

            // Estilo para cabeçalhos
            final CellStyle headerStyle = workbook.createCellStyle();
            final Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Cabeçalho
            final Row headerRow = sheet.createRow(0);
            final Cell cell0 = headerRow.createCell(0);
            cell0.setCellValue("Estudantes");
            cell0.setCellStyle(headerStyle);

            for (int i = 0; i < dates.size(); i++) {
                final Cell cell = headerRow.createCell(i + 1);
                cell.setCellValue(new SimpleDateFormat("dd/MM/yyyy").format(dates.get(i)));
                cell.setCellStyle(headerStyle);
            }

            // Linhas dos alunos
            int rowIdx = 1;
            for (final var student : students) {
                final Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(student.getName());

                final Map<Date, AttendanceStatusEnum> studentRecords = attendanceMap.getOrDefault(student.getId(), Collections.emptyMap());

                for (int i = 0; i < dates.size(); i++) {
                    final Date date = dates.get(i);
                    final AttendanceStatusEnum status = studentRecords.get(date);

                    final String display = Objects.nonNull(status) ? status.getDescription() : AttendanceStatusEnum.ABSENT.getDescription();

                    row.createCell(i + 1).setCellValue(display);
                }
            }

            // Ajusta o tamanho das colunas
            for (int i = 0; i <= dates.size(); i++) {
                sheet.autoSizeColumn(i);
            }

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

}
