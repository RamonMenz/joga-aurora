package br.feevale.joga_aurora.repository;

import br.feevale.joga_aurora.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentEntity, String> {
}