package br.feevale.joga_aurora.controller;

import br.feevale.joga_aurora.enums.GenderEnum;
import br.feevale.joga_aurora.filter.StudentFilter;
import br.feevale.joga_aurora.model.Student;
import br.feevale.joga_aurora.service.StudentService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.Objects;

@AllArgsConstructor
@RestController
@RequestMapping("/estudante")
public class StudentController {

    private final StudentService service;

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false, value = "nome") final String name,
            @RequestParam(required = false, value = "data_nascimento_ini") final Date birthDateStart,
            @RequestParam(required = false, value = "data_nascimento_fim") final Date birthDateEnd,
            @RequestParam(required = false, value = "genero") final String gender,
            @RequestParam(required = false, value = "turma_id") final String classroomId,
            final Pageable pageable) {
        final var filter = new StudentFilter(name, birthDateStart, birthDateEnd, GenderEnum.fromCode(gender), classroomId);

        final var studentPage = service.getAll(filter, pageable);

        return ResponseEntity.ok(studentPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable final String id) {
        final var student = service.getById(id);

        if (Objects.nonNull(student))
            return ResponseEntity.ok(student);

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> insert(@RequestBody final Student request) {
        final var student = service.insert(request);

        if (Objects.nonNull(student))
            return ResponseEntity.status(HttpStatus.CREATED).body(student);

        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable final String id, @RequestBody final Student request) {
        final var student = service.update(id, request);

        if (Objects.nonNull(student))
            return ResponseEntity.ok(student);

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
