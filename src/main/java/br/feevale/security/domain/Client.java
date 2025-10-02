package br.feevale.security.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = "id")
public class Client {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(columnDefinition = "varchar(36)")
    private String id;

    @Column(name = "name", columnDefinition = "varchar(256)", nullable = false)
    private String name;

    @Column(name = "email", columnDefinition = "varchar(256)", nullable = false)
    private String email;

    @Column(name = "password", columnDefinition = "varchar(128)", nullable = false)
    private String password;

    @Column(name = "active", columnDefinition = "boolean", nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Permission> permissions = new ArrayList<>();

    public void addPermission(Permission permission) {
        this.permissions.add(permission);
        permission.setClient(this);
    }

}
