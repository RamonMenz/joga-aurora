package br.feevale.security.mapper;

import br.feevale.security.controller.request.ClientRequest;
import br.feevale.security.controller.response.ClientResponse;
import br.feevale.security.domain.Client;
import br.feevale.security.domain.Permission;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ClientMapper {

    public static Client toEntity(ClientRequest request) {
        Client entity = new Client();
        entity.setName(request.getName());
        entity.setEmail(request.getEmail());
        return entity;
    }

    public static ClientResponse toResponse(Client entity) {
        return ClientResponse.builder()
                .name(entity.getName())
                .email(entity.getEmail())
                .active(entity.isActive())
                .permissions(buildPermissionsResponse(entity.getPermissions()))
                .build();
    }

    public static List<String> buildPermissionsResponse(List<Permission> permissions) {
        return permissions.stream()
                .map(Permission::getName)
                .collect(toList());
    }
}
