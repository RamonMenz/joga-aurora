package br.feevale.joga_aurora.model;

import br.feevale.joga_aurora.enums.GenderEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;
import java.util.List;

public record Student(

        String id,

        @JsonProperty("nome")
        String name,

        @JsonProperty("data_nascimento")
        Date birthDate,

        @JsonProperty("genero")
        GenderEnum gender,

        @JsonProperty("medidas_corporais")
        List<BodyMeasurement> bodyMeasurements,

        @JsonProperty("testes_fisicos")
        List<PhysicalTest> physicalTests,

        @JsonProperty("turma")
        Classroom classroom

) {}
