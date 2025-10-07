package br.feevale.security.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientNameRequest {

    @NotBlank
    @Min(5)
    private String name;

}
