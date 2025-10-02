package br.feevale.security.service;

import br.feevale.security.domain.Client;
import br.feevale.security.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class RemoveClientService {

    private AuthenticatedClientService authenticatedClientService;
    private ClientRepository clientRepository;

    @Transactional
    public void remove() {
        Client authenticatedClient = authenticatedClientService.get();
        clientRepository.delete(authenticatedClient);
    }

}
