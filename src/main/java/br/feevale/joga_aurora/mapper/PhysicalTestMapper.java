package br.feevale.joga_aurora.mapper;

import br.feevale.joga_aurora.entity.PhysicalTestEntity;
import br.feevale.joga_aurora.model.PhysicalTest;

import java.util.Objects;

public final class PhysicalTestMapper {

    public PhysicalTestMapper() {
        throw new IllegalStateException("Utility class.");
    }

    public static PhysicalTest toBasicResponse(final PhysicalTestEntity source) {
        if (Objects.isNull(source))
            return null;

        return new PhysicalTest(
                source.getId(),
                StudentMapper.toBasicResponse(source.getStudent()),
                source.getCollectionDate(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    public static PhysicalTest toCompleteResponse(final PhysicalTestEntity source) {
        if (Objects.isNull(source))
            return null;

        return new PhysicalTest(
                source.getId(),
                StudentMapper.toBasicResponse(source.getStudent()),
                source.getCollectionDate(),
                source.getSixMinutesTest(),
                source.getSixMinutesReference(),
                source.getFlexTest(),
                source.getFlexReference(),
                source.getRmlTest(),
                source.getRmlReference(),
                source.getTwentyMetersTest(),
                source.getTwentyMetersReference(),
                source.getThrowTwoKgTest(),
                source.getThrowTwoKgReference());
    }

}
