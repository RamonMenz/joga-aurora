package br.feevale.security.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class ClientNameRequest {

    @NotBlank
    @Length(min = 5)
    private String name;

}
