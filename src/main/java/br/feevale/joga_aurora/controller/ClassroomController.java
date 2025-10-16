package br.feevale.joga_aurora.controller;

import br.feevale.joga_aurora.model.Classroom;
import br.feevale.joga_aurora.service.ClassroomAttendanceReportService;
import br.feevale.joga_aurora.service.ClassroomService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.Objects;

@AllArgsConstructor
@RestController
@RequestMapping("/turma")
public class ClassroomController {

    private final ClassroomService service;
    private final ClassroomAttendanceReportService classroomAttendanceReportService;

    @GetMapping
    public ResponseEntity<?> getAll(final Pageable pageable) {
        final var classroomPage = service.getAll(pageable);

        return ResponseEntity.ok(classroomPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable final String id) {
        final var classroom = service.getById(id);

        if (Objects.nonNull(classroom))
            return ResponseEntity.ok(classroom);

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> insert(@RequestBody final Classroom request) {
        final var classroom = service.insert(request);

        if (Objects.nonNull(classroom))
            return ResponseEntity.status(HttpStatus.CREATED).body(classroom);

        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable final String id, @RequestBody final Classroom request) {
        final var classroom = service.update(id, request);

        if (Objects.nonNull(classroom))
            return ResponseEntity.ok(classroom);

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable final String id) {
        final var deleted = service.delete(id);

        if (deleted)
            return ResponseEntity.noContent().build();

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/presenca/relatorio")
    public ResponseEntity<?> getAttendancesReport(@PathVariable final String id,
                                                  @RequestParam(name = "dataInicial") final Date startDate,
                                                  @RequestParam(name = "dataFinal") final Date endDate) {
        final var report = classroomAttendanceReportService.getAttendancesReport(id, startDate, endDate);

        if (Objects.nonNull(report))
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attendance_report.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(report);

        return ResponseEntity.badRequest().build();
    }

}
