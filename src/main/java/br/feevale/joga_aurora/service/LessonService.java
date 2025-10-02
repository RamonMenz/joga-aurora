package br.feevale.joga_aurora.service;

import br.feevale.joga_aurora.entity.LessonEntity;
import br.feevale.joga_aurora.model.Lesson;
import br.feevale.joga_aurora.repository.LessonRepository;
import br.feevale.joga_aurora.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

import static br.feevale.joga_aurora.enums.LogStatusEnum.FINISHED;
import static br.feevale.joga_aurora.enums.LogStatusEnum.STARTED;

@Slf4j
@AllArgsConstructor
@Service
public class LessonService {

    private final LessonRepository repository;

    public Page<LessonEntity> getAll(final Pageable pageable) {
        final var start = Instant.now();
        log.info("status={} pageable={}", STARTED, pageable);

        final var result = repository.findAll(pageable);

        log.info("status={} pageable={} resultSize={} timeMillis={}", FINISHED, pageable, result.getTotalElements(), Duration.between(start, Instant.now()).toMillis());
        return result;
    }

    public LessonEntity getById(final String id) {
        final var start = Instant.now();
        log.info("status={} id={}", STARTED, id);

        final var result = repository.findById(id)
                .orElse(null);

        log.info("status={} id={} result={} timeMillis={}", FINISHED, id, JsonUtil.objectToJson(result), Duration.between(start, Instant.now()).toMillis());
        return result;
    }

    public LessonEntity insert(final Lesson request) {
        final var start = Instant.now();
        log.info("status={} request={}", STARTED, JsonUtil.objectToJson(request));

        final var entity = new LessonEntity();
        entity.setClassroom(request.classroom());
        entity.setLessonDate(request.lessonDate());

        final var result = repository.save(entity);

        log.info("status={} request={} result={} timeMillis={}", FINISHED, JsonUtil.objectToJson(request), JsonUtil.objectToJson(result), Duration.between(start, Instant.now()).toMillis());
        return result;
    }

    public LessonEntity update(final String id, final Lesson request) {
        final var start = Instant.now();
        log.info("status={} id={} request={}", STARTED, id, JsonUtil.objectToJson(request));

        final var result = repository.findById(id)
                .map(it -> {
                    it.setClassroom(request.classroom());
                    it.setLessonDate(request.lessonDate());
                    return repository.save(it);
                }).orElse(null);

        log.info("status={} id={} request={} result={} timeMillis={}", FINISHED, id, JsonUtil.objectToJson(request), JsonUtil.objectToJson(result), Duration.between(start, Instant.now()).toMillis());
        return result;
    }

    public boolean delete(final String id) {
        final var start = Instant.now();
        log.info("status={} id={}", STARTED, id);

        final var result = repository.findById(id)
                .map(classroom -> {
                    repository.delete(classroom);
                    return true;
                })
                .orElse(false);

        log.info("status={} id={} result={} timeMillis={}", FINISHED, id, result, Duration.between(start, Instant.now()).toMillis());
        return result;
    }

}
