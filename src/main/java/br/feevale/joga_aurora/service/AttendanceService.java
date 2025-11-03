package br.feevale.joga_aurora.service;

import br.feevale.joga_aurora.entity.AttendanceEntity;
import br.feevale.joga_aurora.enums.AttendanceStatusEnum;
import br.feevale.joga_aurora.enums.DeletedEnum;
import br.feevale.joga_aurora.enums.NotFoundEnum;
import br.feevale.joga_aurora.mapper.AttendanceMapper;
import br.feevale.joga_aurora.model.Attendance;
import br.feevale.joga_aurora.repository.AttendanceRepository;
import br.feevale.joga_aurora.repository.ClassroomRepository;
import br.feevale.joga_aurora.repository.StudentRepository;
import br.feevale.joga_aurora.util.DateUtil;
import br.feevale.joga_aurora.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import static br.feevale.joga_aurora.enums.LogStatusEnum.FINISHED;
import static br.feevale.joga_aurora.enums.LogStatusEnum.STARTED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@AllArgsConstructor
@Service
public class AttendanceService {

    private final AttendanceRepository repository;
    private final ClassroomRepository classroomRepository;
    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public Page<Attendance> getAll(final Pageable pageable) {
        final var start = Instant.now();
        log.info("status={} pageable={}", STARTED, pageable);

        final var result = repository.findAll(pageable);

        final var response = result.map(AttendanceMapper::toResponse);

        log.info("status={} pageable={} responseSize={} timeMillis={}", FINISHED, pageable, response.getNumberOfElements(), Duration.between(start, Instant.now()).toMillis());
        return response;
    }

    @Transactional(readOnly = true)
    public Attendance getById(final String id) {
        final var start = Instant.now();
        log.info("status={} id={}", STARTED, id);

        final var result = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, NotFoundEnum.ATTENDANCE.getMessage()));

        final var response = AttendanceMapper.toResponse(result);

        log.info("status={} id={} response={} timeMillis={}", FINISHED, id, JsonUtil.objectToJson(response), Duration.between(start, Instant.now()).toMillis());
        return response;
    }

    @Transactional
    public Attendance insert(final Attendance request) {
        final var start = Instant.now();
        log.info("status={} request={}", STARTED, JsonUtil.objectToJson(request));

        final var result = saveAttendanceEntity(new AttendanceEntity(), request);

        final var response = AttendanceMapper.toResponse(result);

        log.info("status={} request={} response={} timeMillis={}", FINISHED, JsonUtil.objectToJson(request), JsonUtil.objectToJson(response), Duration.between(start, Instant.now()).toMillis());
        return response;
    }

    @Transactional
    public Attendance update(final String id, final Attendance request) {
        final var start = Instant.now();
        log.info("status={} id={} request={}", STARTED, id, JsonUtil.objectToJson(request));

        final var result = repository.findById(id)
                .map(it -> saveAttendanceEntity(it, request))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, NotFoundEnum.ATTENDANCE.getMessage()));

        final var response = AttendanceMapper.toResponse(result);

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

    @Transactional(readOnly = true)
    public List<Attendance> getAllByClassroomId(final String id, final Date attendanceDate) {
        final var start = Instant.now();
        final var date = DateUtil.thisDateOrToday(attendanceDate);
        log.info("status={} id={} attendanceDate={}", STARTED, id, date);
        classroomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, NotFoundEnum.CLASSROOM.getMessage()));

        final var result = repository.findByStudent_Classroom_IdAndAttendanceDate(id, date);

        log.info("status={} id={} attendanceDate={} responseSize={} timeMillis={}", FINISHED, id, date, result.size(), Duration.between(start, Instant.now()).toMillis());
        return result.stream().map(AttendanceMapper::toResponse).toList();
    }

    @Transactional
    public List<Attendance> insertByClassroomId(final String id, final Date attendanceDate, final List<Attendance> request) {
        final var start = Instant.now();
        log.info("status={} id={} attendanceDate={} requestSize={}", STARTED, id, attendanceDate, request.size());

        final var classroomEntity = classroomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, NotFoundEnum.CLASSROOM.getMessage()));

        final var result = classroomEntity.getStudentList().stream()
                .map(student -> {
                    final var attendance = getAttendanceByStudentId(request, student.getId());

                    if (Objects.isNull(attendance))
                        return null;

                    return saveAttendanceEntity(
                            new AttendanceEntity(),
                            new Attendance(
                                    null,
                                    attendance.student(),
                                    Objects.nonNull(attendance.attendanceDate())
                                            ? attendance.attendanceDate()
                                            : DateUtil.thisDateOrToday(attendanceDate),
                                    Objects.nonNull(attendance.status())
                                            ? attendance.status()
                                            : AttendanceStatusEnum.PRESENT));
                })
                .filter(Objects::nonNull)
                .toList();

        log.info("status={} id={} attendanceDate={} requestSize={} responseSize={} timeMillis={}", FINISHED, id, attendanceDate, request.size(), result.size(), Duration.between(start, Instant.now()).toMillis());
        return result.stream().map(AttendanceMapper::toResponse).toList();
    }

    @Transactional
    public List<Attendance> updateByClassroomId(final String id, final Date attendanceDate, final List<Attendance> request) {
        final var start = Instant.now();
        log.info("status={} id={} attendanceDate={} requestSize={}", STARTED, id, attendanceDate, request.size());

        final var classroomEntity = classroomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, NotFoundEnum.CLASSROOM.getMessage()));

        final var result = classroomEntity.getStudentList().stream()
                .map(student -> {
                    final var attendance = getAttendanceByStudentId(request, student.getId());

                    if (Objects.isNull(attendance))
                        return null;

                    final var attendanceEntity = repository
                            .findByStudent_IdAndAttendanceDate(student.getId(), attendanceDate).orElse(null);

                    final var status = Objects.nonNull(attendance.status())
                            ? attendance.status()
                            : AttendanceStatusEnum.LATE;

                    final var date = Objects.nonNull(attendance.attendanceDate())
                            ? attendance.attendanceDate()
                            : DateUtil.thisDateOrToday(attendanceDate);

                    if (Objects.nonNull(attendanceEntity))
                        return saveAttendanceEntity(
                                attendanceEntity,
                                new Attendance(
                                        attendanceEntity.getId(),
                                        attendance.student(),
                                        date,
                                        status));

                    return saveAttendanceEntity(
                            new AttendanceEntity(),
                            new Attendance(
                                    null,
                                    attendance.student(),
                                    date,
                                    status));
                })
                .filter(Objects::nonNull)
                .toList();

        log.info("status={} id={} attendanceDate={} requestSize={} responseSize={} timeMillis={}", FINISHED, id, attendanceDate, request.size(), result.size(), Duration.between(start, Instant.now()).toMillis());
        return result.stream().map(AttendanceMapper::toResponse).toList();
    }

    protected AttendanceEntity saveAttendanceEntity(final AttendanceEntity entity, final Attendance request) {
        if (Objects.nonNull(request.student()))
            studentRepository.findById(request.student().id()).ifPresent(entity::setStudent);

        entity.setAttendanceDate(DateUtil.thisDateOrToday(request.attendanceDate()));
        entity.setStatus(request.status());

        return repository.save(entity);
    }

    private Attendance getAttendanceByStudentId(final List<Attendance> attendanceList, final String studentId) {
        if (Objects.nonNull(studentId))
            for (final var attendance : attendanceList)
                if (Objects.nonNull(attendance.student()) && attendance.student().id().equals(studentId))
                    return attendance;

        return null;
    }

}
