package br.feevale.joga_aurora.enums.converter;

import br.feevale.joga_aurora.enums.AttendanceStatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Optional;

@Converter(autoApply = true)
public class AttendanceStatusEnumConverter implements AttributeConverter<AttendanceStatusEnum, String> {

    @Override
    public String convertToDatabaseColumn(final AttendanceStatusEnum attendanceStatus) {
        return Optional.ofNullable(attendanceStatus).map(AttendanceStatusEnum::getCode).orElse(null);
    }

    @Override
    public AttendanceStatusEnum convertToEntityAttribute(final String dbData) {
        return Optional.ofNullable(dbData).map(AttendanceStatusEnum::fromCode).orElse(AttendanceStatusEnum.ABSENT);
    }

}
