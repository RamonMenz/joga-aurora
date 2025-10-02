package br.feevale.joga_aurora.enums.converter;

import br.feevale.joga_aurora.enums.RiskReferenceEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Optional;

@Converter(autoApply = true)
public class RiskReferenceEnumConverter implements AttributeConverter<RiskReferenceEnum, String> {

    @Override
    public String convertToDatabaseColumn(final RiskReferenceEnum attribute) {
        return Optional.ofNullable(attribute).map(RiskReferenceEnum::getCode).orElse(null);
    }

    @Override
    public RiskReferenceEnum convertToEntityAttribute(final String dbData) {
        return Optional.ofNullable(dbData).map(RiskReferenceEnum::fromCode).orElse(null);
    }

}
