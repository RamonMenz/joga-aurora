package br.feevale.security.service;

import br.feevale.security.controller.response.ClientResponse;
import br.feevale.security.domain.Client;
import br.feevale.security.mapper.ClientMapper;
import br.feevale.security.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static java.util.Objects.isNull;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@AllArgsConstructor
@Service
public class FindClientService {

    private ClientRepository clientRepository;
    private AuthenticatedClientService authenticatedClientService;

    public ClientResponse find() {
        Client authenticatedClient = authenticatedClientService.get();
        return ClientMapper.toResponse(authenticatedClient);
    }

    public Client findById(String id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Client not found"));
    }

    public Client findByEmail(String email) {
        Client client = clientRepository.findByEmail(email);

        if (isNull(client)) {
            throw new ResponseStatusException(NOT_FOUND, "Client not found");
        }

        return client;
    }

    public Page<ClientResponse> findAllByName(String text, boolean inactiveOnly, Pageable pageable) {
        if (inactiveOnly) {
            return clientRepository.findInactiveByNameOrEmail(text, pageable)
                    .map(ClientMapper::toResponse);
        } else {
            return clientRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrderByNameAsc(text, text, pageable)
                    .map(ClientMapper::toResponse);
        }
    }

}
