package br.feevale.joga_aurora.mapper;

import br.feevale.joga_aurora.entity.AttendanceEntity;
import br.feevale.joga_aurora.model.Attendance;

import java.util.Objects;

public final class AttendanceMapper {

    public AttendanceMapper() {
        throw new IllegalStateException("Utility class.");
    }

    public static Attendance toResponse(final AttendanceEntity source) {
        if (Objects.isNull(source))
            return null;

        return new Attendance(
                source.getId(),
                StudentMapper.toBasicResponseWithoutClassroom(source.getStudent()),
                source.getAttendanceDate(),
                source.getStatus());
    }

}
