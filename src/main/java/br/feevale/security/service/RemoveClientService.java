package br.feevale.security.service;

import br.feevale.security.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class RemoveClientService {

    private final AuthenticatedClientService authenticatedClientService;
    private final ClientRepository clientRepository;

    @Transactional
    public void remove() {
        final var authenticatedClient = authenticatedClientService.get();
        clientRepository.delete(authenticatedClient);
    }

}
