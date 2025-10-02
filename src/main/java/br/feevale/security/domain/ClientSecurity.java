package br.feevale.security.domain;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

public class ClientSecurity implements UserDetails {

    private static final String SPRING_PERMISSION_PREFIX = "ROLE_";

    @Getter
    private final String id;

    private final String email;
    private final String password;
    private final boolean active;
    private final List<SimpleGrantedAuthority> permissions;

    public ClientSecurity(Client client) {
        this.id = client.getId();
        this.email = client.getEmail();
        this.password = client.getPassword();
        this.active = client.isActive();
        this.permissions = convertPermissions(client);
    }

    private List<SimpleGrantedAuthority> convertPermissions(Client client) {
        return client.getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(SPRING_PERMISSION_PREFIX + permission.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<SimpleGrantedAuthority> getAuthorities() {
        return this.permissions;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.active;
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }

}
