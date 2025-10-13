package br.feevale.joga_aurora.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderEnum {

    NOT_INFORMED("N", "Não informado"),
    MALE("M", "Masculino"),
    FEMALE("F", "Feminino"),
    ;

    private final String code;
    private final String description;

    @JsonCreator
    public static GenderEnum fromCode(final String code) {
        for (final var g : values()) {
            if (g.code.equals(code.toUpperCase())) {
                return g;
            }
        }
        return NOT_INFORMED;
    }

    @JsonValue
    public String getDescription() {
        return this.description;
    }

}
