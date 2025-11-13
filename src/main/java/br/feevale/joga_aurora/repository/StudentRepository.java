package br.feevale.joga_aurora.repository;

import br.feevale.joga_aurora.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface StudentRepository extends JpaRepository<StudentEntity, String>, JpaSpecificationExecutor<StudentEntity> {

    List<StudentEntity> findByClassroom_IdOrderByNameAsc(final String classroomId);

}