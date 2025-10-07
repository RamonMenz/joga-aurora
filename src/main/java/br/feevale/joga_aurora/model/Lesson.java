package br.feevale.joga_aurora.model;

import br.feevale.joga_aurora.entity.ClassroomEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;

public record Lesson(

        String id,

        @JsonProperty("turma")
        ClassroomEntity classroom,

        @JsonProperty("data_aula")
        Date lessonDate

) {}
