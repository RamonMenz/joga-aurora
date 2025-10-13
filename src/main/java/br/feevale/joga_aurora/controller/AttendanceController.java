package br.feevale.joga_aurora.controller;

import br.feevale.joga_aurora.model.Attendance;
import br.feevale.joga_aurora.service.AttendanceService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@AllArgsConstructor
@RestController
@RequestMapping("/presenca")
public class AttendanceController {

    private final AttendanceService service;

    @GetMapping
    public ResponseEntity<?> getAll(final Pageable pageable) {
        final var attendancePage = service.getAll(pageable);

        return ResponseEntity.ok(attendancePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable final String id) {
        final var attendance = service.getById(id);

        if (Objects.nonNull(attendance))
            return ResponseEntity.ok(attendance);

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> insert(@RequestBody final Attendance request) {
        final var attendance = service.insert(request);

        if (Objects.nonNull(attendance))
            return ResponseEntity.status(HttpStatus.CREATED).body(attendance);

        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable final String id, @RequestBody final Attendance request) {
        final var attendance = service.update(id, request);

        if (Objects.nonNull(attendance))
            return ResponseEntity.ok(attendance);

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable final String id) {
        final var deleted = service.delete(id);

        if (deleted)
            return ResponseEntity.noContent().build();

        return ResponseEntity.notFound().build();
    }

}
