package br.feevale.joga_aurora.service;

import br.feevale.joga_aurora.entity.BodyMeasurementEntity;
import br.feevale.joga_aurora.enums.DeletedEnum;
import br.feevale.joga_aurora.enums.NotFoundEnum;
import br.feevale.joga_aurora.mapper.BodyMeasurementMapper;
import br.feevale.joga_aurora.model.BodyMeasurement;
import br.feevale.joga_aurora.repository.BodyMeasurementRepository;
import br.feevale.joga_aurora.repository.StudentRepository;
import br.feevale.joga_aurora.util.DateUtil;
import br.feevale.joga_aurora.util.JsonUtil;
import br.feevale.joga_aurora.util.ReferenceUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

import static br.feevale.joga_aurora.enums.LogStatusEnum.FINISHED;
import static br.feevale.joga_aurora.enums.LogStatusEnum.STARTED;
import static br.feevale.joga_aurora.enums.ReferenceEnum.BMI;
import static br.feevale.joga_aurora.enums.ReferenceEnum.WAIST_HEIGHT_RATIO;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@AllArgsConstructor
@Service
public class BodyMeasurementService {

    private final BodyMeasurementRepository repository;
    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public Page<BodyMeasurement> getAll(final Pageable pageable) {
        final var start = Instant.now();
        log.info("status={} pageable={}", STARTED, pageable);

        final var result = repository.findAll(pageable);

        final var response = result.map(BodyMeasurementMapper::toCompleteResponse);

        log.info("status={} pageable={} responseSize={} timeMillis={}", FINISHED, pageable, response.getNumberOfElements(), Duration.between(start, Instant.now()).toMillis());
        return response;
    }

    @Transactional(readOnly = true)
    public BodyMeasurement getById(final String id) {
        final var start = Instant.now();
        log.info("status={} id={}", STARTED, id);

        final var result = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, NotFoundEnum.BODY_MEASUREMENT.getMessage()));

        final var response = BodyMeasurementMapper.toCompleteResponse(result);

        log.info("status={} id={} response={} timeMillis={}", FINISHED, id, JsonUtil.objectToJson(response), Duration.between(start, Instant.now()).toMillis());
        return response;
    }

    @Transactional
    public BodyMeasurement insert(final BodyMeasurement request) {
        final var start = Instant.now();
        log.info("status={} request={}", STARTED, JsonUtil.objectToJson(request));

        final var result = saveBodyMeasurementEntity(new BodyMeasurementEntity(), request);

        final var response = BodyMeasurementMapper.toCompleteResponse(result);

        log.info("status={} request={} response={} timeMillis={}", FINISHED, JsonUtil.objectToJson(request), JsonUtil.objectToJson(response), Duration.between(start, Instant.now()).toMillis());
        return response;
    }

    @Transactional
    public BodyMeasurement update(final String id, final BodyMeasurement request) {
        final var start = Instant.now();
        log.info("status={} id={} request={}", STARTED, id, JsonUtil.objectToJson(request));

        final var result = repository.findById(id)
                .map(it -> saveBodyMeasurementEntity(it, request))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, NotFoundEnum.BODY_MEASUREMENT.getMessage()));

        final var response = BodyMeasurementMapper.toCompleteResponse(result);

        log.info("status={} id={} request={} response={} timeMillis={}", FINISHED, id, JsonUtil.objectToJson(request), JsonUtil.objectToJson(response), Duration.between(start, Instant.now()).toMillis());
        return response;
    }

    @Transactional
    public boolean delete(final String id) {
        final var start = Instant.now();
        log.info("status={} id={}", STARTED, id);

        final var result = repository.findById(id)
                .map(it -> {
                    repository.delete(it);
                    return true;
                }).orElse(false);

        final var response = result ? DeletedEnum.DELETED : DeletedEnum.NOT_FOUND;

        log.info("status={} id={} response={} timeMillis={}", FINISHED, id, response, Duration.between(start, Instant.now()).toMillis());
        return result;
    }

    private BodyMeasurementEntity saveBodyMeasurementEntity(final BodyMeasurementEntity entity, final BodyMeasurement request) {
        if (Objects.nonNull(request.student()))
            studentRepository.findById(request.student().id()).ifPresentOrElse(
                    entity::setStudent,
                    () -> {
                        throw new ResponseStatusException(NOT_FOUND, NotFoundEnum.STUDENT.getMessage());
                    });

        entity.setCollectionDate(DateUtil.thisDateOrToday(request.collectionDate()));
        entity.setWaist(request.waist());
        entity.setWeight(request.weight());
        entity.setHeight(request.height());

        final var bmi = calculateBmi(request.weight(), request.height());
        entity.setBmi(bmi);
        entity.setBmiReference(ReferenceUtil.getReference(BMI, bmi, entity.getStudent()));

        final var waistHeightRatio = calculateWaistHeightRatio(request.waist(), request.height());
        entity.setWaistHeightRatio(waistHeightRatio);
        entity.setWaistHeightRatioReference(ReferenceUtil.getReference(WAIST_HEIGHT_RATIO, waistHeightRatio, entity.getStudent()));

        return repository.save(entity);
    }

    private Double calculateBmi(final Double weight, final Integer height) {
        return BigDecimal.valueOf(weight / Math.pow((double) height / 100.0, 2)).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private Double calculateWaistHeightRatio(final Integer waist, final Integer height) {
        return BigDecimal.valueOf((double) waist / height).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

}
