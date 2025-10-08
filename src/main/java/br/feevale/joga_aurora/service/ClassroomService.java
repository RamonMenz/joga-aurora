package br.feevale.joga_aurora.service;

import br.feevale.joga_aurora.entity.ClassroomEntity;
import br.feevale.joga_aurora.enums.DeletedEnum;
import br.feevale.joga_aurora.mapper.ClassroomMapper;
import br.feevale.joga_aurora.model.Classroom;
import br.feevale.joga_aurora.repository.ClassroomRepository;
import br.feevale.joga_aurora.repository.LessonRepository;
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

@Slf4j
@AllArgsConstructor
@Service
public class ClassroomService {

    private final ClassroomRepository repository;
    private final LessonRepository lessonRepository;

    @Transactional(readOnly = true)
    public Page<Classroom> getAll(final Pageable pageable) {
        final var start = Instant.now();
        log.info("status={} pageable={}", STARTED, pageable);

        final var result = repository.findAll(pageable);

        final var response = result.map(ClassroomMapper::toBasicResponse);

        log.info("status={} pageable={} responseSize={} timeMillis={}", FINISHED, pageable, response.getNumberOfElements(), Duration.between(start, Instant.now()).toMillis());
        return response;
    }

    @Transactional(readOnly = true)
    public Classroom getById(final String id) {
        final var start = Instant.now();
        log.info("status={} id={}", STARTED, id);

        final var result = repository.findById(id).orElse(null);

        boolean attendanceDone = lessonRepository.existsByClassroom_IdAndLessonDate(Objects.requireNonNull(result).getId(), Date.valueOf(LocalDate.now()));

        final var response = ClassroomMapper.toCompleteResponse(result, attendanceDone);

        log.info("status={} id={} response={} timeMillis={}", FINISHED, id, JsonUtil.objectToJson(response), Duration.between(start, Instant.now()).toMillis());
        return response;
    }

    @Transactional
    public Classroom insert(final Classroom request) {
        final var start = Instant.now();
        log.info("status={} request={}", STARTED, JsonUtil.objectToJson(request));

        final var entity = new ClassroomEntity();

        final var result = saveClassroomEntity(entity, request);

        final var response = ClassroomMapper.toCompleteResponse(result, false);

        log.info("status={} request={} response={} timeMillis={}", FINISHED, JsonUtil.objectToJson(request), JsonUtil.objectToJson(response), Duration.between(start, Instant.now()).toMillis());
        return response;
    }

    @Transactional
    public Classroom update(final String id, final Classroom request) {
        final var start = Instant.now();
        log.info("status={} id={} request={}", STARTED, id, JsonUtil.objectToJson(request));

        final var result = repository.findById(id)
                .map(it -> saveClassroomEntity(it, request)).orElse(null);

        boolean attendanceDone = lessonRepository.existsByClassroom_IdAndLessonDate(Objects.requireNonNull(result).getId(), Date.valueOf(LocalDate.now()));

        final var response = ClassroomMapper.toCompleteResponse(result, attendanceDone);

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

    private ClassroomEntity saveClassroomEntity(final ClassroomEntity entity, final Classroom request) {
        entity.setName(request.name());
        entity.setYear(request.year());

        return repository.save(entity);
    }

}
