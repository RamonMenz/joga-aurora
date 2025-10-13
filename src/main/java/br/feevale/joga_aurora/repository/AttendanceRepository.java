package br.feevale.joga_aurora.repository;

import br.feevale.joga_aurora.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<AttendanceEntity, String> {

    boolean existsByStudent_Classroom_IdAndAttendanceDate(final String studentClassroomId, final Date attendanceDate);

    List<AttendanceEntity> findByStudent_Classroom_IdAndAttendanceDate(final String studentClassroomId, final Date attendanceDate);

}