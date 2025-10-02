package br.feevale.joga_aurora.repository;

import br.feevale.joga_aurora.entity.AttendanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<AttendanceEntity, String> {
}