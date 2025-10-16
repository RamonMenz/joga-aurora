package br.feevale.joga_aurora.service;

import br.feevale.joga_aurora.entity.AttendanceEntity;
import br.feevale.joga_aurora.enums.AttendanceStatusEnum;
import br.feevale.joga_aurora.mapper.AttendanceMapper;
import br.feevale.joga_aurora.mapper.ClassroomMapper;
import br.feevale.joga_aurora.model.Attendance;
import br.feevale.joga_aurora.model.ClassroomAttendance;
import br.feevale.joga_aurora.repository.AttendanceRepository;
import br.feevale.joga_aurora.repository.ClassroomRepository;
import br.feevale.joga_aurora.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

import static br.feevale.joga_aurora.enums.LogStatusEnum.FINISHED;
import static br.feevale.joga_aurora.enums.LogStatusEnum.STARTED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@AllArgsConstructor
@Service
public class ClassroomAttendanceService {

    private static final String CLASSROOM_NOT_FOUND_MESSAGE = "Turma não encontrada";
    private final ClassroomRepository classroomRepository;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceService attendanceService;

    @Transactional(readOnly = true)
    public ClassroomAttendance getAll(final String id, final Date attendanceDate) {
        final var start = Instant.now();
        final var date = DateUtil.thisDateOrToday(attendanceDate);
        log.info("status={} id={} attendanceDate={}", STARTED, id, date);

        final var classroomEntity = classroomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, CLASSROOM_NOT_FOUND_MESSAGE));

        final var result = attendanceRepository.findByStudent_Classroom_IdAndAttendanceDate(id, date);

        final var response = new ClassroomAttendance(
                ClassroomMapper.toBasicResponse(classroomEntity),
                result.stream().map(AttendanceMapper::toResponse).toList());

        log.info("status={} id={} attendanceDate={} responseSize={} timeMillis={}", FINISHED, id, date, response.attendanceList().size(), Duration.between(start, Instant.now()).toMillis());
        return response;
    }

    @Transactional
    public ClassroomAttendance insert(final String id, final Date attendanceDate, final ClassroomAttendance request) {
        final var start = Instant.now();
        log.info("status={} id={} attendanceDate={} requestSize={}", STARTED, id, attendanceDate, request.attendanceList().size());

        final var classroomEntity = classroomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, CLASSROOM_NOT_FOUND_MESSAGE));

        final var result = classroomEntity.getStudentList().stream()
                .map(student -> {
                    final var attendance = request.getAttendanceByStudentId(student.getId());

                    if (Objects.isNull(attendance))
                        return null;

                    return attendanceService.saveAttendanceEntity(
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

        final var response = new ClassroomAttendance(
                ClassroomMapper.toBasicResponse(classroomEntity),
                result.stream().map(AttendanceMapper::toResponse).toList());

        log.info("status={} id={} attendanceDate={} requestSize={} responseSize={} timeMillis={}", FINISHED, id, attendanceDate, request.attendanceList().size(), response.attendanceList().size(), Duration.between(start, Instant.now()).toMillis());
        return response;
    }

    @Transactional
    public ClassroomAttendance update(final String id, final Date attendanceDate, final ClassroomAttendance request) {
        final var start = Instant.now();
        log.info("status={} id={} attendanceDate={} requestSize={}", STARTED, id, attendanceDate, request.attendanceList().size());

        final var classroomEntity = classroomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, CLASSROOM_NOT_FOUND_MESSAGE));

        final var result = classroomEntity.getStudentList().stream()
                .map(student -> {
                    final var attendance = request.getAttendanceByStudentId(student.getId());

                    if (Objects.isNull(attendance))
                        return null;

                    final var attendanceEntity = attendanceRepository
                            .findByStudent_IdAndAttendanceDate(student.getId(), attendanceDate).orElse(null);

                    if (Objects.nonNull(attendanceEntity))
                        return attendanceService.saveAttendanceEntity(
                                attendanceEntity,
                                new Attendance(
                                        attendanceEntity.getId(),
                                        attendance.student(),
                                        Objects.nonNull(attendance.attendanceDate())
                                                ? attendance.attendanceDate()
                                                : DateUtil.thisDateOrToday(attendanceDate),
                                        Objects.nonNull(attendance.status())
                                                ? attendance.status()
                                                : AttendanceStatusEnum.LATE));

                    return attendanceService.saveAttendanceEntity(
                            new AttendanceEntity(),
                            new Attendance(
                                    null,
                                    attendance.student(),
                                    Objects.nonNull(attendance.attendanceDate())
                                            ? attendance.attendanceDate()
                                            : DateUtil.thisDateOrToday(attendanceDate),
                                    Objects.nonNull(attendance.status())
                                            ? attendance.status()
                                            : AttendanceStatusEnum.LATE));
                })
                .filter(Objects::nonNull)
                .toList();

        final var response = new ClassroomAttendance(
                ClassroomMapper.toBasicResponse(classroomEntity),
                result.stream().map(AttendanceMapper::toResponse).toList());

        log.info("status={} id={} attendanceDate={} requestSize={} responseSize={} timeMillis={}", FINISHED, id, attendanceDate, request.attendanceList().size(), response.attendanceList().size(), Duration.between(start, Instant.now()).toMillis());
        return response;
    }

}
