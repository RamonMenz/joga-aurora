package br.feevale.joga_aurora.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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

    @JsonCreator
    public static RiskReferenceEnum fromCode(final String code) {
        for (final var r : values()) {
            if (r.code.equals(code)) {
                return r;
            }
        }
        return null;
    }

    @JsonValue
    public String getDescription() {
        return this.description;
    }

}
