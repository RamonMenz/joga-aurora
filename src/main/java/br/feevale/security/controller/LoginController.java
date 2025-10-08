package br.feevale.security.controller;

import br.feevale.security.controller.response.ClientResponse;
import br.feevale.security.service.FindClientService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/login")
public class LoginController {

    private final FindClientService findClientService;

    @PostMapping
    public ClientResponse login() {
        return findClientService.find();
    }

}
