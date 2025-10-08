package br.feevale.security.mapper;

import br.feevale.security.controller.request.ClientRequest;
import br.feevale.security.controller.response.ClientResponse;
import br.feevale.security.domain.Client;
import br.feevale.security.domain.Permission;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ClientMapper {

    public ClientMapper() {
        throw new IllegalStateException("Utility class.");
    }

    public static Client toEntity(final ClientRequest request) {
        final var entity = new Client();
        entity.setName(request.getName());
        return entity;
    }

    public static ClientResponse toResponse(final Client entity) {
        return ClientResponse.builder()
                .name(entity.getName())
                .active(entity.isActive())
                .permissions(buildPermissionsResponse(entity.getPermissions()))
                .build();
    }

    public static List<String> buildPermissionsResponse(final List<Permission> permissions) {
        return permissions.stream()
                .map(Permission::getName)
                .collect(toList());
    }

}
