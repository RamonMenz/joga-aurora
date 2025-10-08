package br.feevale.security.service;

import br.feevale.security.domain.Client;
import br.feevale.security.domain.ClientSecurity;
import br.feevale.security.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@AllArgsConstructor
@Service
public class AuthenticatedClientService {

    private static final String USER_NOT_FOUND_MESSAGE = "Usuário não existente ou não autenticado";
    private final ClientRepository clientRepository;

    public String getId() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        final var clientSecurity = (ClientSecurity) authentication.getPrincipal();
        return clientSecurity.getId();
    }

    public Client get() {
        return clientRepository.findById(getId())
                .orElseThrow(() -> new ResponseStatusException(INTERNAL_SERVER_ERROR, USER_NOT_FOUND_MESSAGE));
    }

}
