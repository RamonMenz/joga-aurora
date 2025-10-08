package br.feevale.joga_aurora.service;

import br.feevale.joga_aurora.entity.PhysicalTestEntity;
import br.feevale.joga_aurora.entity.StudentEntity;
import br.feevale.joga_aurora.enums.DeletedEnum;
import br.feevale.joga_aurora.enums.RiskReferenceEnum;
import br.feevale.joga_aurora.mapper.PhysicalTestMapper;
import br.feevale.joga_aurora.model.PhysicalTest;
import br.feevale.joga_aurora.repository.PhysicalTestRepository;
import br.feevale.joga_aurora.repository.ReferenceTable;
import br.feevale.joga_aurora.repository.StudentRepository;
import br.feevale.joga_aurora.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

import static br.feevale.joga_aurora.enums.LogStatusEnum.FINISHED;
import static br.feevale.joga_aurora.enums.LogStatusEnum.STARTED;
import static br.feevale.joga_aurora.enums.RiskReferenceEnum.NO_RISK;
import static br.feevale.joga_aurora.enums.RiskReferenceEnum.RISK;

@Slf4j
@AllArgsConstructor
@Service
public class PhysicalTestService {

    private static final ReferenceTable REFERENCE_TABLE = new ReferenceTable();
    private final PhysicalTestRepository repository;
    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public Page<PhysicalTest> getAll(final Pageable pageable) {
        final var start = Instant.now();
        log.info("status={} pageable={}", STARTED, pageable);

        final var result = repository.findAll(pageable);

        final var response = result.map(PhysicalTestMapper::toCompleteResponse);

        log.info("status={} pageable={} responseSize={} timeMillis={}", FINISHED, pageable, response.getNumberOfElements(), Duration.between(start, Instant.now()).toMillis());
        return response;
    }

    @Transactional(readOnly = true)
    public PhysicalTest getById(final String id) {
        final var start = Instant.now();
        log.info("status={} id={}", STARTED, id);

        final var result = repository.findById(id).orElse(null);

        final var response = PhysicalTestMapper.toCompleteResponse(result);

        log.info("status={} id={} response={} timeMillis={}", FINISHED, id, JsonUtil.objectToJson(response), Duration.between(start, Instant.now()).toMillis());
        return response;
    }

    @Transactional
    public PhysicalTest insert(final PhysicalTest request) {
        final var start = Instant.now();
        log.info("status={} request={}", STARTED, JsonUtil.objectToJson(request));

        final var entity = new PhysicalTestEntity();

        final var result = savePhysicalTestEntity(entity, request);

        final var response = PhysicalTestMapper.toCompleteResponse(result);

        log.info("status={} request={} response={} timeMillis={}", FINISHED, JsonUtil.objectToJson(request), JsonUtil.objectToJson(response), Duration.between(start, Instant.now()).toMillis());
        return response;
    }

    @Transactional
    public PhysicalTest update(final String id, final PhysicalTest request) {
        final var start = Instant.now();
        log.info("status={} id={} request={}", STARTED, id, JsonUtil.objectToJson(request));

        final var result = repository.findById(id)
                .map(it -> savePhysicalTestEntity(it, request)).orElse(null);

        final var response = PhysicalTestMapper.toCompleteResponse(result);

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

    private PhysicalTestEntity savePhysicalTestEntity(final PhysicalTestEntity entity, final PhysicalTest request) {
        if (Objects.nonNull(request.student()))
            studentRepository.findById(request.student().id()).ifPresent(entity::setStudent);

        entity.setCollectionDate(Objects.nonNull(request.collectionDate()) ? request.collectionDate() : Date.valueOf(LocalDate.now()));

        entity.setSixMinutesTest(request.sixMinutesTest());
        entity.setSixMinutesReference(calculateSixMinutesReference(request.sixMinutesTest(), entity.getStudent()));

        entity.setFlexTest(request.flexTest());
        entity.setFlexReference(calculateFlexReference(request.flexTest(), entity.getStudent()));

        entity.setRmlTest(request.rmlTest());
        entity.setRmlReference(calculateRmlReference(request.rmlTest(), entity.getStudent()));

        entity.setTwentyMetersTest(request.twentyMetersTest());
        entity.setTwentyMetersReference(calculateTwentyMetersReference(request.twentyMetersTest(), entity.getStudent()));

        entity.setThrowTwoKgTest(request.throwTwoKgTest());
        entity.setThrowTwoKgReference(calculateThrowTwoKgReference(request.throwTwoKgTest(), entity.getStudent()));

        return repository.save(entity);
    }

    private static RiskReferenceEnum calculateSixMinutesReference(final Double sixMinutes, final StudentEntity student) {
        return sixMinutes < REFERENCE_TABLE.getReference(student).sixMinutes() ? RISK : NO_RISK;
    }

    private static RiskReferenceEnum calculateFlexReference(final Double flex, final StudentEntity student) {
        return flex < REFERENCE_TABLE.getReference(student).flex() ? RISK : NO_RISK;
    }

    private static RiskReferenceEnum calculateRmlReference(final Double rml, final StudentEntity student) {
        return rml < REFERENCE_TABLE.getReference(student).rml() ? RISK : NO_RISK;
    }

    private static RiskReferenceEnum calculateTwentyMetersReference(final Double twentyMeters, final StudentEntity student) {
        return twentyMeters > REFERENCE_TABLE.getReference(student).twentyMeters() ? RISK : NO_RISK;
    }

    private static RiskReferenceEnum calculateThrowTwoKgReference(final Double throwTwoKg, final StudentEntity student) {
        return throwTwoKg < REFERENCE_TABLE.getReference(student).throwTwoKg() ? RISK : NO_RISK;
    }

}
