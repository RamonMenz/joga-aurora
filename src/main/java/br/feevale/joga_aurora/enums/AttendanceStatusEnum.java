package br.feevale.joga_aurora.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AttendanceStatusEnum {

    PRESENT("P", "Presente"),
    ABSENT("A", "Ausente"),
    LATE("L", "Atrasado");

    private final String code;
    private final String description;

    @JsonCreator
    public static AttendanceStatusEnum fromCode(final String code) {
        for (final var a : values()) {
            if (a.code.equals(code)) {
                return a;
            }
        }
        return ABSENT;
    }

    @JsonValue
    public String getDescription() {
        return this.description;
    }

}
