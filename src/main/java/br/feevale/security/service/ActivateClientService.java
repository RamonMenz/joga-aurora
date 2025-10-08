package br.feevale.security.service;

import br.feevale.security.controller.request.ClientNameRequest;
import br.feevale.security.controller.response.ClientResponse;
import br.feevale.security.mapper.ClientMapper;
import br.feevale.security.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class ActivateClientService {

    private final FindClientService findClientService;
    private final ClientRepository clientRepository;

    @Transactional
    public ClientResponse changeActiveStatus(final ClientNameRequest request) {
        final var client = findClientService.findByName(request.getName());

        client.setActive(!client.isActive());

        final var result = clientRepository.save(client);

        return ClientMapper.toResponse(result);
    }

}
