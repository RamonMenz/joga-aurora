package br.feevale.joga_aurora.mapper;

import br.feevale.joga_aurora.entity.StudentEntity;
import br.feevale.joga_aurora.model.Student;

import java.util.Collections;
import java.util.Objects;

public final class StudentMapper {

    public StudentMapper() {
        throw new IllegalStateException("Utility class.");
    }

    public static Student toBasicResponse(final StudentEntity source) {
        if (Objects.isNull(source))
            return null;

        return new Student(
                source.getId(),
                source.getName(),
                null,
                null,
                null,
                null,
                ClassroomMapper.toBasicResponse(source.getClassroom()));
    }

    public static Student toBasicResponseWithoutClassroom(final StudentEntity source) {
        if (Objects.isNull(source))
            return null;

        return new Student(
                source.getId(),
                source.getName(),
                null,
                null,
                null,
                null,
                null);
    }

    public static Student toCompleteResponse(final StudentEntity source) {
        if (Objects.isNull(source))
            return null;

        return new Student(
                source.getId(),
                source.getName(),
                source.getBirthDate(),
                source.getGender(),
                Objects.isNull(source.getBodyMeasurementList())
                        ? Collections.emptyList()
                        : source.getBodyMeasurementList().stream()
                        .map(BodyMeasurementMapper::toBasicResponse).toList(),
                Objects.isNull(source.getPhysicalTestList())
                        ? Collections.emptyList()
                        : source.getPhysicalTestList().stream()
                        .map(PhysicalTestMapper::toBasicResponse).toList(),
                ClassroomMapper.toBasicResponse(source.getClassroom()));
    }

}
