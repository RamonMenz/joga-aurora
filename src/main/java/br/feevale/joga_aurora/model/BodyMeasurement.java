package br.feevale.joga_aurora.model;

import br.feevale.joga_aurora.enums.RiskReferenceEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BodyMeasurement(

        String id,

        @JsonProperty("estudante")
        Student student,

        @JsonProperty("data_coleta")
        Date collectionDate,

        @JsonProperty("cintura")
        Integer waist,

        @JsonProperty("peso")
        Double weight,

        @JsonProperty("estatura")
        Integer height,

        @JsonProperty("imc")
        Double bmi,

        @JsonProperty("referencia_imc")
        RiskReferenceEnum bmiReference,

        @JsonProperty("relacao_cintura_estatura")
        Double waistHeightRatio,

        @JsonProperty("referencia_relacao_cintura_estatura")
        RiskReferenceEnum waistHeightRatioReference

) {
}
