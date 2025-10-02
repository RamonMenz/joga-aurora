package br.feevale.security.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientEmailRequest {

    @NotBlank
    @Email
    private String email;

}
