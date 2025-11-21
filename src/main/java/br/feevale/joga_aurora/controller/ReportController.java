package br.feevale.joga_aurora.controller;

import br.feevale.joga_aurora.service.AttendanceReportService;
import br.feevale.joga_aurora.service.StudentReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.Objects;

@AllArgsConstructor
@RestController
@RequestMapping("/relatorio")
public class ReportController {

    private final AttendanceReportService attendanceReportService;
    private final StudentReportService studentReportService;

    @GetMapping("/presenca/turma/{id}")
    public ResponseEntity<?> getAttendancesReport(@PathVariable final String id,
                                                  @RequestParam(name = "dataInicial") final Date startDate,
                                                  @RequestParam(name = "dataFinal") final Date endDate) {
        final var report = attendanceReportService.getAttendancesReport(id, startDate, endDate);

        if (Objects.nonNull(report))
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attendance_report.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(report);

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/estudantes/turma/{id}")
    public ResponseEntity<?> getStudentsReport(@PathVariable final String id,
                                                  @RequestParam(name = "dataInicial") final Date startDate,
                                                  @RequestParam(name = "dataFinal") final Date endDate) {
        final var report = studentReportService.getStudentsReport(id, startDate, endDate);

        if (Objects.nonNull(report))
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=students_report.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(report);

        return ResponseEntity.badRequest().build();
    }

}
