package br.feevale.joga_aurora.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DeletedEnum {

    DELETED("deleted"),
    NOT_FOUND("not_found"),
    ;

    final String name;

    @Override
    public final String toString() {
        return this.getName();
    }

}
