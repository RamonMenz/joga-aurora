package br.feevale.joga_aurora.model;

import br.feevale.joga_aurora.enums.RiskReferenceEnum;

import java.sql.Date;

public record BodyMeasurement(
        String id,
        Student student,
        Date collectionDate,
        Double waist,
        Double weight,
        Integer height,
        Double bmi,
        RiskReferenceEnum bmiReference,
        Double waistHeightRatio,
        RiskReferenceEnum waistHeightReference
) {}
