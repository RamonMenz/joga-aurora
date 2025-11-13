package br.feevale.joga_aurora.filter;

import br.feevale.joga_aurora.entity.StudentEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class StudentSpecification {

    public static Specification<StudentEntity> filterBy(final StudentFilter filter) {
        return (root, query, cb) -> {
            final var predicates = new ArrayList<Predicate>();

            if (filter.name() != null && !filter.name().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + filter.name().toLowerCase() + "%"));
            }

            if (filter.birthDateStart() != null && filter.birthDateEnd() != null) {
                predicates.add(cb.between(root.get("birthDate"), filter.birthDateStart(), filter.birthDateEnd()));
            } else if (filter.birthDateStart() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("birthDate"), filter.birthDateStart()));
            } else if (filter.birthDateEnd() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("birthDate"), filter.birthDateEnd()));
            }

            if (filter.gender() != null) {
                predicates.add(cb.equal(root.get("gender"), filter.gender()));
            }

            if (filter.classroomId() != null && !filter.classroomId().isBlank()) {
                predicates.add(cb.equal(root.get("classroom").get("id"), filter.classroomId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
