package br.feevale.security.service;

import br.feevale.security.domain.Client;
import br.feevale.security.domain.ClientSecurity;
import br.feevale.security.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@AllArgsConstructor
@Service
public class AuthenticatedClientService {

    private ClientRepository clientRepository;

    public String getId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClientSecurity clientSecurity = (ClientSecurity) authentication.getPrincipal();
        return clientSecurity.getId();
    }

    public Client get() {
        return clientRepository.findById(getId())
                .orElseThrow(() -> new ResponseStatusException(INTERNAL_SERVER_ERROR, "User does not exist or is not authenticated"));
    }

}
