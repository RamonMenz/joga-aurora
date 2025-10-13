package br.feevale.joga_aurora.repository;

import br.feevale.joga_aurora.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<StudentEntity, String> {

    List<StudentEntity> findByClassroom_IdOrderByNameAsc(final String classroomId);

}