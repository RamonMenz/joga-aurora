package br.feevale.joga_aurora.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ClassroomAttendance(

        @JsonProperty("turma")
        Classroom classroom,

        @JsonProperty("lista_presenca")
        List<Attendance> attendanceList

) {
}
