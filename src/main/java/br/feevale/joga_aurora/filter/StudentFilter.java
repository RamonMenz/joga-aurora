package br.feevale.joga_aurora.filter;

import br.feevale.joga_aurora.enums.GenderEnum;

import java.sql.Date;

public record StudentFilter(

        String name,
        Date birthDateStart,
        Date birthDateEnd,
        GenderEnum gender,
        String classroomId

) {
}
