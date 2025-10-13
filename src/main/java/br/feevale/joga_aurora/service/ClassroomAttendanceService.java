package br.feevale.joga_aurora.service;

import br.feevale.joga_aurora.entity.AttendanceEntity;
import br.feevale.joga_aurora.mapper.AttendanceMapper;
import br.feevale.joga_aurora.mapper.ClassroomMapper;
import br.feevale.joga_aurora.model.ClassroomAttendance;
import br.feevale.joga_aurora.repository.AttendanceRepository;
import br.feevale.joga_aurora.repository.ClassroomRepository;
import br.feevale.joga_aurora.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.Duration;
import java.time.Instant;

import static br.feevale.joga_aurora.enums.LogStatusEnum.FINISHED;
import static br.feevale.joga_aurora.enums.LogStatusEnum.STARTED;

@Slf4j
@AllArgsConstructor
@Service
public class ClassroomAttendanceService {

    private final ClassroomRepository repository;
    private final AttendanceRepository attendanceRepository;
    private final AttendanceService attendanceService;

    @Transactional(readOnly = true)
    public ClassroomAttendance getAll(final String id, final Date attendanceDate) {
        final var start = Instant.now();
        final var date = DateUtil.thisDateOrToday(attendanceDate);
        log.info("status={} id={} attendanceDate={}", STARTED, id, date);

        final var classroomEntity = repository.findById(id).orElse(null);

        final var result = attendanceRepository.findByStudent_Classroom_IdAndAttendanceDate(id, date);

        final var response = new ClassroomAttendance(
                ClassroomMapper.toBasicResponse(classroomEntity),
                result.stream().map(AttendanceMapper::toResponse).toList());

        log.info("status={} id={} attendanceDate={} responseSize={} timeMillis={}", FINISHED, id, date, response.attendanceList().size(), Duration.between(start, Instant.now()).toMillis());
        return response;
    }

    @Transactional
    public ClassroomAttendance insert(final String id, final ClassroomAttendance request) {
        final var start = Instant.now();
        log.info("status={} id={} requestSize={}", STARTED, id, request.attendanceList().size());

        final var classroomEntity = repository.findById(id).orElse(null);

        final var result = request.attendanceList().stream()
                .map(it -> attendanceService.saveAttendanceEntity(new AttendanceEntity(), it)).toList();

        final var response = new ClassroomAttendance(
                ClassroomMapper.toBasicResponse(classroomEntity),
                result.stream().map(AttendanceMapper::toResponse).toList());

        log.info("status={} id={} requestSize={} responseSize={} timeMillis={}", FINISHED, id, request.attendanceList().size(), response.attendanceList().size(), Duration.between(start, Instant.now()).toMillis());
        return response;
    }

    @Transactional
    public ClassroomAttendance update(final String id, final ClassroomAttendance request) {
        final var start = Instant.now();
        log.info("status={} id={} requestSize={}", STARTED, id, request.attendanceList().size());

        final var classroomEntity = repository.findById(id).orElse(null);

        final var result = request.attendanceList().stream()
                .map(it ->
                        attendanceRepository.findById(it.id())
                                .map(attendanceEntity ->
                                        attendanceService.saveAttendanceEntity(attendanceEntity, it))
                                .orElse(null))
                .toList();

        final var response = new ClassroomAttendance(
                ClassroomMapper.toBasicResponse(classroomEntity),
                result.stream().map(AttendanceMapper::toResponse).toList());

        log.info("status={} id={} requestSize={} responseSize={} timeMillis={}", FINISHED, id, request.attendanceList().size(), response.attendanceList().size(), Duration.between(start, Instant.now()).toMillis());
        return response;
    }

}
