package br.feevale.security.repository;

import br.feevale.security.domain.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, String> {

    Optional<Client> findByName(final String name);

    Page<Client> findByNameContainingIgnoreCaseOrderByNameAsc(final String name, final Pageable pageable);

    @Query("SELECT c FROM Client c " +
            "WHERE c.active = false " +
            "AND LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "ORDER BY c.name ASC")
    Page<Client> findInactiveByName(final String name, final Pageable pageable);

}
