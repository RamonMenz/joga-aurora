package br.feevale.security.service;

import br.feevale.security.controller.request.ClientRequest;
import br.feevale.security.controller.response.ClientResponse;
import br.feevale.security.domain.Client;
import br.feevale.security.domain.Permission;
import br.feevale.security.mapper.ClientMapper;
import br.feevale.security.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class AddClientService {

    private ClientRepository clientRepository;
    private PasswordEncoder passwordEncoder;

    @Transactional
    public ClientResponse add(ClientRequest request) {
        Client client = ClientMapper.toEntity(request);
        client.setPassword(passwordEncoder.encode(request.getPassword()));
        client.setActive(false);
        client.addPermission(Permission.builder().name("ADMIN").build());

        clientRepository.save(client);

        return ClientMapper.toResponse(client);
    }

}
