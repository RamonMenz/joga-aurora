package br.feevale.joga_aurora.enums.converter;

import br.feevale.joga_aurora.enums.GenderEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Optional;

@Converter(autoApply = true)
public class GenderEnumConverter implements AttributeConverter<GenderEnum, String> {

    @Override
    public String convertToDatabaseColumn(final GenderEnum gender) {
        return Optional.ofNullable(gender).map(GenderEnum::getCode).orElse(null);
    }

    @Override
    public GenderEnum convertToEntityAttribute(final String dbData) {
        return Optional.ofNullable(dbData).map(GenderEnum::fromCode).orElse(GenderEnum.NOT_INFORMED);
    }

}
