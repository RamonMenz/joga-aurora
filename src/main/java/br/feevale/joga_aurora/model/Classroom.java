package br.feevale.joga_aurora.model;

import java.util.List;

public record Classroom(
        String id,
        String name,
        Integer year,
        Boolean attendanceDone,
        List<Student> students
) {}
