package br.feevale.security.repository;

import br.feevale.security.domain.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClientRepository extends JpaRepository<Client, String> {

    Client findByEmail(String email);

    Page<Client> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrderByNameAsc(String name, String email, Pageable pageable);

    @Query("SELECT c FROM Client c " +
            "WHERE c.active = false " +
            "AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(c.email) LIKE LOWER(CONCAT('%', :text, '%'))) " +
            "ORDER BY c.name ASC")
    Page<Client> findInactiveByNameOrEmail(String text, Pageable pageable);

}
