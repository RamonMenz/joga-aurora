package br.feevale.joga_aurora.model;

import br.feevale.joga_aurora.entity.StudentEntity;
import br.feevale.joga_aurora.enums.RiskReferenceEnum;

import java.sql.Date;

public record PhysicalTest(
        String id,
        StudentEntity student,
        Date collectionDate,
        Double sixMinutesTest,
        RiskReferenceEnum sixMinutesReference,
        Double flexTest,
        RiskReferenceEnum flexReference,
        Double rmlTest,
        RiskReferenceEnum rmlReference,
        Double twentyMetersTest,
        RiskReferenceEnum twentyMetersReference,
        Double throwTwoKgTest,
        RiskReferenceEnum throwTwoKgReference
) {}
