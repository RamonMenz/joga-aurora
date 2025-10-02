package br.feevale.joga_aurora.repository;

import br.feevale.joga_aurora.entity.LessonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;

public interface LessonRepository extends JpaRepository<LessonEntity, String> {

    boolean existsByClassroom_IdAndLessonDate(final String classroomId, final Date lessonDate);

}