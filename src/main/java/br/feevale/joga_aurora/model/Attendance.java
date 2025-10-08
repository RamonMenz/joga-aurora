package br.feevale.joga_aurora.model;

import br.feevale.joga_aurora.enums.AttendanceStatusEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Attendance (

    String id,

    @JsonProperty("estudante")
    Student student,

    @JsonProperty("aula")
    Lesson lesson,

    @JsonProperty("status")
    AttendanceStatusEnum status

) {}
