package br.feevale.joga_aurora.model;

import br.feevale.joga_aurora.enums.AttendanceStatusEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Attendance(

        String id,

        @JsonProperty("estudante")
        Student student,

        @JsonProperty("data_presenca")
        Date attendanceDate,

        @JsonProperty("status")
        AttendanceStatusEnum status

) {
}
