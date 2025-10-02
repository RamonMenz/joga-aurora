package br.feevale.joga_aurora.model;

import br.feevale.joga_aurora.enums.GenderEnum;

import java.sql.Date;
import java.util.List;

public record Student(
        String id,
        String name,
        Date birthDate,
        GenderEnum gender,
        List<BodyMeasurement> bodyMeasurementList,
        List<PhysicalTest> physicalTestList,
        Classroom classroom
) {}
