package br.feevale.joga_aurora.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RiskReferenceEnum {

    RISK("R", "Com Risco"),
    NO_RISK("N", "Sem Risco"),
    ;

    private final String code;
    private final String description;

    public static RiskReferenceEnum fromCode(final String code) {
        for (RiskReferenceEnum r : values()) {
            if (r.code.equals(code)) {
                return r;
            }
        }
        return null;
    }

}
