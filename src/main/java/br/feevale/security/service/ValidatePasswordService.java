package br.feevale.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class ValidatePasswordService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void validate(String currentPassword, String encryptedPassword) {
        if (!passwordEncoder.matches(currentPassword, encryptedPassword)) {
            throw new ResponseStatusException(BAD_REQUEST, "Senha incorreta");
        }
    }
}
