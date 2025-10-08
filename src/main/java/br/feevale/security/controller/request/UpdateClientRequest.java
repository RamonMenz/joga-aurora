package br.feevale.security.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateClientRequest {

    @NotBlank
    @Min(5)
    private String name;

    @NotBlank
    @Min(8)
    private String currentPassword;

    @Min(8)
    private String newPassword;

}
