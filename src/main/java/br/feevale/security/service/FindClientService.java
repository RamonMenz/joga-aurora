package br.feevale.security.service;

import br.feevale.security.controller.response.ClientResponse;
import br.feevale.security.domain.Client;
import br.feevale.security.mapper.ClientMapper;
import br.feevale.security.repository.ClientRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@AllArgsConstructor
@Service
public class FindClientService {

    private static final String USER_NOT_FOUND_MESSAGE = "Usuário não encontrado";
    private static final String USER_FOUND_MESSAGE = "Usuário encontrado: {}";
    private final ClientRepository clientRepository;
    private final AuthenticatedClientService authenticatedClientService;

    public ClientResponse find() {
        final var authenticatedClient = authenticatedClientService.get();

        log.info(USER_FOUND_MESSAGE, authenticatedClient.getName());
        return ClientMapper.toResponse(authenticatedClient);
    }

    public Client findByName(final String name) {
        return clientRepository.findByName(name)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, USER_NOT_FOUND_MESSAGE));
    }

    public Page<ClientResponse> findAllByName(final String name, final boolean inactiveOnly, final Pageable pageable) {
        if (inactiveOnly) {
            return clientRepository.findByNameContainingIgnoreCaseOrderByNameAsc(name, pageable)
                    .map(ClientMapper::toResponse);
        }

        return clientRepository.findByActiveIsFalseAndNameContainingIgnoreCaseOrderByNameAsc(name, pageable)
                    .map(ClientMapper::toResponse);
    }

}
