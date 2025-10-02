package br.feevale.joga_aurora.repository;

import br.feevale.joga_aurora.entity.BodyMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BodyMeasurementRepository extends JpaRepository<BodyMeasurementEntity, String> {
}