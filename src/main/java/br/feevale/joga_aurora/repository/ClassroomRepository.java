package br.feevale.joga_aurora.repository;

import br.feevale.joga_aurora.entity.ClassroomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomRepository extends JpaRepository<ClassroomEntity, String> {
}