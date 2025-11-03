package br.feevale.joga_aurora.service;

import br.feevale.joga_aurora.entity.PhysicalTestEntity;
import br.feevale.joga_aurora.enums.DeletedEnum;
import br.feevale.joga_aurora.enums.NotFoundEnum;
import br.feevale.joga_aurora.mapper.PhysicalTestMapper;
import br.feevale.joga_aurora.model.PhysicalTest;
import br.feevale.joga_aurora.repository.PhysicalTestRepository;
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

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

import static br.feevale.joga_aurora.enums.LogStatusEnum.FINISHED;
import static br.feevale.joga_aurora.enums.LogStatusEnum.STARTED;
import static br.feevale.joga_aurora.enums.ReferenceEnum.FLEX;
import static br.feevale.joga_aurora.enums.ReferenceEnum.RML;
import static br.feevale.joga_aurora.enums.ReferenceEnum.SIX_MINUTES;
import static br.feevale.joga_aurora.enums.ReferenceEnum.THROW_TWO_KG;
import static br.feevale.joga_aurora.enums.ReferenceEnum.TWENTY_METERS;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@AllArgsConstructor
@Service
public class PhysicalTestService {

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

        final var result = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, NotFoundEnum.PHYSICAL_TEST.getMessage()));

        final var response = PhysicalTestMapper.toCompleteResponse(result);

        log.info("status={} id={} response={} timeMillis={}", FINISHED, id, JsonUtil.objectToJson(response), Duration.between(start, Instant.now()).toMillis());
        return response;
    }

    @Transactional
    public PhysicalTest insert(final PhysicalTest request) {
        final var start = Instant.now();
        log.info("status={} request={}", STARTED, JsonUtil.objectToJson(request));

        final var result = savePhysicalTestEntity(new PhysicalTestEntity(), request);

        final var response = PhysicalTestMapper.toCompleteResponse(result);

        log.info("status={} request={} response={} timeMillis={}", FINISHED, JsonUtil.objectToJson(request), JsonUtil.objectToJson(response), Duration.between(start, Instant.now()).toMillis());
        return response;
    }

    @Transactional
    public PhysicalTest update(final String id, final PhysicalTest request) {
        final var start = Instant.now();
        log.info("status={} id={} request={}", STARTED, id, JsonUtil.objectToJson(request));

        final var result = repository.findById(id)
                .map(it -> savePhysicalTestEntity(it, request))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, NotFoundEnum.PHYSICAL_TEST.getMessage()));

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
            studentRepository.findById(request.student().id()).ifPresentOrElse(
                    entity::setStudent,
                    () -> {
                        throw new ResponseStatusException(NOT_FOUND, NotFoundEnum.STUDENT.getMessage());
                    });

        entity.setCollectionDate(DateUtil.thisDateOrToday(request.collectionDate()));

        entity.setSixMinutesTest(request.sixMinutesTest());
        entity.setSixMinutesReference(ReferenceUtil.getReference(SIX_MINUTES, request.sixMinutesTest(), entity.getStudent()));

        entity.setFlexTest(request.flexTest());
        entity.setFlexReference(ReferenceUtil.getReference(FLEX, request.flexTest(), entity.getStudent()));

        entity.setRmlTest(request.rmlTest());
        entity.setRmlReference(ReferenceUtil.getReference(RML, request.rmlTest(), entity.getStudent()));

        entity.setTwentyMetersTest(request.twentyMetersTest());
        entity.setTwentyMetersReference(ReferenceUtil.getReference(TWENTY_METERS, request.twentyMetersTest(), entity.getStudent()));

        entity.setThrowTwoKgTest(request.throwTwoKgTest());
        entity.setThrowTwoKgReference(ReferenceUtil.getReference(THROW_TWO_KG, request.throwTwoKgTest(), entity.getStudent()));

        return repository.save(entity);
    }

}
