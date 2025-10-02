package br.feevale.security.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateClientRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;

}
