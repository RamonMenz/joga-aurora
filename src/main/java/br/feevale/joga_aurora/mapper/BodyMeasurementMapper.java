package br.feevale.joga_aurora.mapper;

import br.feevale.joga_aurora.entity.BodyMeasurementEntity;
import br.feevale.joga_aurora.model.BodyMeasurement;

import java.util.Objects;

public final class BodyMeasurementMapper {

    public BodyMeasurementMapper() {
        throw new IllegalStateException("Utility class.");
    }

    public static BodyMeasurement toBasicResponse(final BodyMeasurementEntity source) {
        if (Objects.isNull(source))
            return null;

        return new BodyMeasurement(
                source.getId(),
                StudentMapper.toBasicResponse(source.getStudent()),
                source.getCollectionDate(),
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    public static BodyMeasurement toCompleteResponse(final BodyMeasurementEntity source) {
        if (Objects.isNull(source))
            return null;

        return new BodyMeasurement(
                source.getId(),
                StudentMapper.toBasicResponse(source.getStudent()),
                source.getCollectionDate(),
                source.getWaist(),
                source.getWeight(),
                source.getHeight(),
                source.getBmi(),
                source.getBmiReference(),
                source.getWaistHeightRatio(),
                source.getWaistHeightRatioReference());
    }

}
