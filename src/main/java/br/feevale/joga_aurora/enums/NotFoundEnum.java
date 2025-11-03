package br.feevale.joga_aurora.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotFoundEnum {

    ATTENDANCE("Presença não encontrada"),
    BODY_MEASUREMENT("Medida Corporal não encontrada"),
    CLASSROOM("Turma não encontrada"),
    PHYSICAL_TEST("Teste Físico não encontrado"),
    STUDENT("Estudante não encontrado"),
    ;

    final String message;
}
