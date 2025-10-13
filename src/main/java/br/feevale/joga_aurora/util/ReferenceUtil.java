package br.feevale.joga_aurora.util;

import br.feevale.joga_aurora.entity.StudentEntity;
import br.feevale.joga_aurora.enums.ReferenceEnum;
import br.feevale.joga_aurora.enums.RiskReferenceEnum;
import br.feevale.joga_aurora.repository.ReferenceTable;

import static br.feevale.joga_aurora.enums.RiskReferenceEnum.NO_RISK;
import static br.feevale.joga_aurora.enums.RiskReferenceEnum.RISK;

public class ReferenceUtil {

    private static final ReferenceTable REFERENCE_TABLE = new ReferenceTable();

    private ReferenceUtil() {
        throw new IllegalStateException("Utility class.");
    }

    public static RiskReferenceEnum getReference(final ReferenceEnum reference, final Double value, final StudentEntity student) {
        return switch (reference) {
            case BMI -> calculateBmiReference(value, student);
            case WAIST_HEIGHT_RATIO -> calculateWaistHeightRatioReference(value, student);
            case SIX_MINUTES -> calculateSixMinutesReference(value, student);
            case FLEX -> calculateFlexReference(value, student);
            case RML -> calculateRmlReference(value, student);
            case TWENTY_METERS -> calculateTwentyMetersReference(value, student);
            case THROW_TWO_KG -> calculateThrowTwoKgReference(value, student);
        };
    }

    private static RiskReferenceEnum calculateBmiReference(final Double bmi, final StudentEntity student) {
        return bmi > REFERENCE_TABLE.getReference(student).bmi() ? RISK : NO_RISK;
    }

    private static RiskReferenceEnum calculateWaistHeightRatioReference(final Double waistHeightRatio, final StudentEntity student) {
        return waistHeightRatio > REFERENCE_TABLE.getReference(student).waistHeightRatio() ? RISK : NO_RISK;
    }

    private static RiskReferenceEnum calculateSixMinutesReference(final Double sixMinutes, final StudentEntity student) {
        return sixMinutes < REFERENCE_TABLE.getReference(student).sixMinutes() ? RISK : NO_RISK;
    }

    private static RiskReferenceEnum calculateFlexReference(final Double flex, final StudentEntity student) {
        return flex < REFERENCE_TABLE.getReference(student).flex() ? RISK : NO_RISK;
    }

    private static RiskReferenceEnum calculateRmlReference(final Double rml, final StudentEntity student) {
        return rml < REFERENCE_TABLE.getReference(student).rml() ? RISK : NO_RISK;
    }

    private static RiskReferenceEnum calculateTwentyMetersReference(final Double twentyMeters, final StudentEntity student) {
        return twentyMeters > REFERENCE_TABLE.getReference(student).twentyMeters() ? RISK : NO_RISK;
    }

    private static RiskReferenceEnum calculateThrowTwoKgReference(final Double throwTwoKg, final StudentEntity student) {
        return throwTwoKg < REFERENCE_TABLE.getReference(student).throwTwoKg() ? RISK : NO_RISK;
    }

}
