package br.feevale.joga_aurora.repository;

import br.feevale.joga_aurora.entity.PhysicalTestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface PhysicalTestRepository extends JpaRepository<PhysicalTestEntity, String> {

    List<PhysicalTestEntity> findByStudent_Classroom_IdAndCollectionDateBetweenOrderByStudent_NameAscCollectionDateAsc(final String classroomId, final Date startDate, final Date endDate);

}