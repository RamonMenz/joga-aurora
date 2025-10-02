package br.feevale.joga_aurora.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LogStatusEnum {

    STARTED("started"),
    EXECUTED("executed"),
    FINISHED("finished"),
    FAILED("failed"),
    ABORTED("aborted"),
    ;

    final String name;

    @Override
    public final String toString() {
        return this.getName();
    }

}
