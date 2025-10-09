package br.feevale.joga_aurora.mapper;

import br.feevale.joga_aurora.entity.ClassroomEntity;
import br.feevale.joga_aurora.model.Classroom;
import br.feevale.joga_aurora.model.Student;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class ClassroomMapper {

    public ClassroomMapper() {
        throw new IllegalStateException("Utility class.");
    }

    public static Classroom toBasicResponse(final ClassroomEntity source) {
        return getClassroomResponse(source, null, null);
    }

    public static Classroom toCompleteResponse(final ClassroomEntity source, final Boolean attendanceDone) {
        return getClassroomResponse(
                source,
                attendanceDone,
                Objects.isNull(source.getStudentList())
                        ? Collections.emptyList()
                        : source.getStudentList().stream()
                        .map(StudentMapper::toBasicResponse)
                        .toList());
    }

    private static Classroom getClassroomResponse(final ClassroomEntity source, final Boolean attendanceDone, final List<Student> students) {
        if (Objects.isNull(source))
            return null;

        return new Classroom(
                source.getId(),
                source.getName(),
                attendanceDone,
                students);
    }

}
