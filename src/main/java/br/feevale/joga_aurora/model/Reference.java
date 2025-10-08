package br.feevale.joga_aurora.model;

import br.feevale.joga_aurora.enums.GenderEnum;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Reference(

        Integer age,
        GenderEnum gender,
        Double bmi,
        Double waistHeightRatio,
        Double sixMinutes,
        Double flex,
        Double rml,
        Double twentyMeters,
        Double throwTwoKg

) {}
