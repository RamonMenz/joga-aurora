package br.feevale.joga_aurora.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Classroom(

        String id,

        @JsonProperty("nome")
        String name,

        @JsonProperty("ano")
        Integer year,

        @JsonProperty("chamada_feita")
        Boolean attendanceDone,

        @JsonProperty("estudantes")
        List<Student> students

) {}
