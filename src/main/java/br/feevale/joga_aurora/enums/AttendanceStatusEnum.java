package br.feevale.joga_aurora.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AttendanceStatusEnum {

    PRESENT("P", "Presente"),
    ABSENT("A", "Ausente"),
    LATE("L", "Atrasado")
    ;

    private final String code;
    private final String description;

    public static AttendanceStatusEnum fromCode(final String code) {
        for (AttendanceStatusEnum a : values()) {
            if (a.code.equals(code)) {
                return a;
            }
        }
        return ABSENT;
    }

}
