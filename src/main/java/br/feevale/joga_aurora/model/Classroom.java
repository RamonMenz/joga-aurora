package br.feevale.joga_aurora.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Classroom(

        String id,

        @JsonProperty("nome")
        String name,

        @JsonProperty("chamada_feita")
        Boolean attendanceDone,

        @JsonProperty("estudantes")
        List<Student> students

) {
}
