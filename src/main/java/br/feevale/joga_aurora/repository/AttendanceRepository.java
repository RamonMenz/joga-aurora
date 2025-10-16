package br.feevale.joga_aurora.repository;

import br.feevale.joga_aurora.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<AttendanceEntity, String> {

    boolean existsByStudent_Classroom_IdAndAttendanceDate(final String studentClassroomId, final Date attendanceDate);

    List<AttendanceEntity> findByStudent_Classroom_IdAndAttendanceDate(final String studentClassroomId, final Date attendanceDate);

    List<AttendanceEntity> findByStudent_Classroom_IdAndAttendanceDateBetweenOrderByAttendanceDateAsc(final String studentClassroomId, final Date attendanceDateAfter, final Date attendanceDateBefore);

    Optional<AttendanceEntity> findByStudent_IdAndAttendanceDate(final String studentId, final Date attendanceDate);

}