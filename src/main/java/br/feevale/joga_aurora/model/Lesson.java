package br.feevale.joga_aurora.model;

import br.feevale.joga_aurora.entity.ClassroomEntity;

import java.sql.Date;

public record Lesson(
        String id,
        ClassroomEntity classroom,
        Date lessonDate
) {}
