package br.feevale.joga_aurora.controller;

import br.feevale.joga_aurora.service.AttendanceReportService;
import br.feevale.joga_aurora.service.StudentReportService;
import br.feevale.joga_aurora.repository.ClassroomRepository;
import br.feevale.joga_aurora.entity.ClassroomEntity;
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
import java.text.SimpleDateFormat;
import java.util.Objects;

@AllArgsConstructor
@RestController
@RequestMapping("/relatorio")
public class ReportController {

    private final AttendanceReportService attendanceReportService;
    private final StudentReportService studentReportService;
    private final ClassroomRepository classroomRepository;

    @GetMapping("/presenca/turma/{id}")
    public ResponseEntity<?> getAttendancesReport(@PathVariable final String id,
                                                  @RequestParam(name = "dataInicial") final Date startDate,
                                                  @RequestParam(name = "dataFinal") final Date endDate) {
        final var report = attendanceReportService.getAttendancesReport(id, startDate, endDate);

        if (Objects.nonNull(report)) {
            final String classroomName = classroomRepository.findById(id)
                    .map(ClassroomEntity::getName)
                    .orElse("turma");

            final String filename = buildFilename("presenca", classroomName, startDate, endDate);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(report);
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/estudantes/turma/{id}")
    public ResponseEntity<?> getStudentsReport(@PathVariable final String id,
                                                  @RequestParam(name = "dataInicial") final Date startDate,
                                                  @RequestParam(name = "dataFinal") final Date endDate) {
        final var report = studentReportService.getStudentsReport(id, startDate, endDate);

        if (Objects.nonNull(report)) {
            final String classroomName = classroomRepository.findById(id)
                    .map(ClassroomEntity::getName)
                    .orElse("turma");

            final String filename = buildFilename("estudantes", classroomName, startDate, endDate);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(report);
        }

        return ResponseEntity.badRequest().build();
    }

    private String buildFilename(final String prefix, final String classroomName, final Date startDate, final Date endDate) {
        final var sdf = new SimpleDateFormat("dd-MM-yyyy");
        final String start = startDate != null ? sdf.format(startDate) : "start";
        final String end = endDate != null ? sdf.format(endDate) : "end";

        // Sanitize classroom name: trim, replace spaces with underscore, remove non-alphanumeric/underscore/dash
        final String sanitized = classroomName == null ? "turma" : classroomName.trim().replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9_\\-]", "");

        return prefix + "_turma_" + sanitized + "_" + start + "_a_" + end + ".xlsx";
    }

}
