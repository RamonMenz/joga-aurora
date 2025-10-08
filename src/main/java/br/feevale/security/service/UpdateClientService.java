package br.feevale.security.service;

import br.feevale.security.controller.request.UpdateClientRequest;
import br.feevale.security.controller.response.ClientResponse;
import br.feevale.security.mapper.ClientMapper;
import br.feevale.security.repository.ClientRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class UpdateClientService {

    private final AuthenticatedClientService authenticatedClientService;
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    private final ValidatePasswordService validatePasswordService;

    @Transactional
    public ClientResponse update(@Valid final UpdateClientRequest request) {
        final var authenticatedClient = authenticatedClientService.get();
        validatePasswordService.validate(request.getCurrentPassword(), authenticatedClient.getPassword());

        authenticatedClient.setName(request.getName());

        if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
            authenticatedClient.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        final var result = clientRepository.save(authenticatedClient);

        return ClientMapper.toResponse(result);
    }

}
