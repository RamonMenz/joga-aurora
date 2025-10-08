package br.feevale.joga_aurora.service;

import br.feevale.joga_aurora.entity.StudentEntity;
import br.feevale.joga_aurora.enums.DeletedEnum;
import br.feevale.joga_aurora.mapper.StudentMapper;
import br.feevale.joga_aurora.model.Student;
import br.feevale.joga_aurora.repository.ClassroomRepository;
import br.feevale.joga_aurora.repository.StudentRepository;
import br.feevale.joga_aurora.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

import static br.feevale.joga_aurora.enums.LogStatusEnum.FINISHED;
import static br.feevale.joga_aurora.enums.LogStatusEnum.STARTED;

@Slf4j
@AllArgsConstructor
@Service
public class StudentService {

    private final StudentRepository repository;
    private final ClassroomRepository classroomRepository;

    @Transactional(readOnly = true)
    public Page<Student> getAll(final Pageable pageable) {
        final var start = Instant.now();
        log.info("status={} pageable={}", STARTED, pageable);

        final var result = repository.findAll(pageable);

        final var response = result.map(StudentMapper::toBasicResponse);

        log.info("status={} pageable={} responseSize={} timeMillis={}", FINISHED, pageable, response.getNumberOfElements(), Duration.between(start, Instant.now()).toMillis());
        return response;
    }

    @Transactional(readOnly = true)
    public Student getById(final String id) {
        final var start = Instant.now();
        log.info("status={} id={}", STARTED, id);

        final var result = repository.findById(id).orElse(null);

        final var response = StudentMapper.toCompleteResponse(result);

        log.info("status={} id={} response={} timeMillis={}", FINISHED, id, JsonUtil.objectToJson(response), Duration.between(start, Instant.now()).toMillis());
        return response;
    }

    @Transactional
    public Student insert(final Student request) {
        final var start = Instant.now();
        log.info("status={} request={}", STARTED, JsonUtil.objectToJson(request));

        final var entity = new StudentEntity();

        final var result = saveStudentEntity(entity, request);

        final var response = StudentMapper.toCompleteResponse(result);

        log.info("status={} request={} response={} timeMillis={}", FINISHED, JsonUtil.objectToJson(request), JsonUtil.objectToJson(response), Duration.between(start, Instant.now()).toMillis());
        return response;
    }

    @Transactional
    public Student update(final String id, final Student request) {
        final var start = Instant.now();
        log.info("status={} id={} request={}", STARTED, id, JsonUtil.objectToJson(request));

        final var result = repository.findById(id)
                .map(it -> saveStudentEntity(it, request)).orElse(null);

        final var response = StudentMapper.toCompleteResponse(result);

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

    private StudentEntity saveStudentEntity(final StudentEntity entity, final Student request) {
        if (Objects.nonNull(request.classroom()))
            classroomRepository.findById(request.classroom().id()).ifPresent(entity::setClassroom);

        entity.setName(request.name());
        entity.setBirthDate(request.birthDate());
        entity.setGender(request.gender());

        return repository.save(entity);
    }

}
