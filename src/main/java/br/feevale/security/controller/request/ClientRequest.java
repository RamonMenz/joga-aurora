package br.feevale.security.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientRequest {

    @NotBlank
    private String name;

    @NotNull
    @Email
    private String email;

    @NotBlank
    private String password;

}
