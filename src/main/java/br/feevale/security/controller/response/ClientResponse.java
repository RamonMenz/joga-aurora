package br.feevale.security.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientResponse {
    private String name;
    private String email;
    private boolean active;
    private List<String> permissions;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientResponse that = (ClientResponse) o;
        return active == that.active &&
                Objects.equals(name, that.name) &&
                Objects.equals(email, that.email) &&
                Objects.equals(permissions, that.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, active, permissions);
    }
}
