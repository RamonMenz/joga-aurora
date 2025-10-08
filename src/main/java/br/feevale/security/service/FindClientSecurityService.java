package br.feevale.security.service;

import br.feevale.security.domain.ClientSecurity;
import br.feevale.security.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class FindClientSecurityService implements UserDetailsService {

    private final ClientRepository clientRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final var client = clientRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new ClientSecurity(client);
    }

}
