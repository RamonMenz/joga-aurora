package br.feevale.security.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class UpdateClientRequest {

    @NotBlank
    @Length(min = 5)
    private String name;

    @NotBlank
    @Length(min = 8)
    private String currentPassword;

    @Length(min = 8)
    private String newPassword;

}
