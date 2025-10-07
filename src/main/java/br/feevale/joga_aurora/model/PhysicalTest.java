package br.feevale.joga_aurora.model;

import br.feevale.joga_aurora.enums.RiskReferenceEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;

public record PhysicalTest(

        String id,

        @JsonProperty("estudante")
        Student student,

        @JsonProperty("data_coleta")
        Date collectionDate,

        @JsonProperty("teste_seis_minutos")
        Double sixMinutesTest,

        @JsonProperty("referencia_seis_minutos")
        RiskReferenceEnum sixMinutesReference,

        @JsonProperty("teste_flex")
        Double flexTest,

        @JsonProperty("referencia_flex")
        RiskReferenceEnum flexReference,

        @JsonProperty("teste_rml")
        Double rmlTest,

        @JsonProperty("referencia_rml")
        RiskReferenceEnum rmlReference,

        @JsonProperty("teste_vinte_metros")
        Double twentyMetersTest,

        @JsonProperty("referencia_vinte_metros")
        RiskReferenceEnum twentyMetersReference,

        @JsonProperty("teste_arremesso_dois_quilos")
        Double throwTwoKgTest,

        @JsonProperty("referencia_arremesso_dois_quilos")
        RiskReferenceEnum throwTwoKgReference

) {}
