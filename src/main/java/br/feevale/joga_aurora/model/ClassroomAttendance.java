package br.feevale.joga_aurora.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ClassroomAttendance(

        @JsonProperty("turma")
        Classroom classroom,

        @JsonProperty("lista_presenca")
        List<Attendance> attendanceList

) {

    public Attendance getAttendanceByStudentId(final String studentId) {
        if (Objects.nonNull(studentId))
            for (final var attendance : attendanceList)
                if (Objects.nonNull(attendance.student()) && attendance.student().id().equals(studentId))
                    return attendance;

        return null;
    }

}
