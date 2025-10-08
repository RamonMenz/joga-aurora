package br.feevale.security.repository;

import br.feevale.security.domain.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, String> {

    Optional<Client> findByName(final String name);

    Page<Client> findByNameContainingIgnoreCaseOrderByNameAsc(final String name, final Pageable pageable);

    Page<Client> findByActiveIsFalseAndNameContainingIgnoreCaseOrderByNameAsc(final String name, final Pageable pageable);

}
