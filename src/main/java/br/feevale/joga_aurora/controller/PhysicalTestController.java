package br.feevale.joga_aurora.controller;

import br.feevale.joga_aurora.model.PhysicalTest;
import br.feevale.joga_aurora.service.PhysicalTestService;
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
@RequestMapping("/teste-fisico")
public class PhysicalTestController {

    private final PhysicalTestService service;

    @GetMapping
    public ResponseEntity<?> getAll(final Pageable pageable) {
        final var physicalTestPage = service.getAll(pageable);

        return ResponseEntity.ok(physicalTestPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable final String id) {
        final var physicalTest = service.getById(id);

        if (Objects.nonNull(physicalTest))
            return ResponseEntity.ok(physicalTest);

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> insert(@RequestBody final PhysicalTest request) {
        final var physicalTest = service.insert(request);

        if (Objects.nonNull(physicalTest))
            return ResponseEntity.status(HttpStatus.CREATED).body(physicalTest);

        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable final String id, @RequestBody final PhysicalTest request) {
        final var physicalTest = service.update(id, request);

        if (Objects.nonNull(physicalTest))
            return ResponseEntity.ok(physicalTest);

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
