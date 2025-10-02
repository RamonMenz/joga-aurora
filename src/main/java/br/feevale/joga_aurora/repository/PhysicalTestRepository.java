package br.feevale.joga_aurora.repository;

import br.feevale.joga_aurora.entity.PhysicalTestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhysicalTestRepository extends JpaRepository<PhysicalTestEntity, String> {
}