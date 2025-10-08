package br.feevale.security.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@AllArgsConstructor
@Service
public class ValidatePasswordService {

    private final PasswordEncoder passwordEncoder;

    public void validate(final String currentPassword, final String encryptedPassword) {
        if (!passwordEncoder.matches(currentPassword, encryptedPassword)) {
            throw new ResponseStatusException(BAD_REQUEST, "Senha incorreta");
        }
    }
}
