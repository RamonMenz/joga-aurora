package br.feevale.security.service;

import br.feevale.security.controller.request.ClientNameRequest;
import br.feevale.security.controller.response.ClientResponse;
import br.feevale.security.domain.Permission;
import br.feevale.security.mapper.ClientMapper;
import br.feevale.security.repository.ClientRepository;
import br.feevale.security.repository.PermissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class ChangeClientPermissionsService {

    private static final String ADMIN_PERMISSION = "ADMIN";
    private final FindClientService findClientService;
    private final ClientRepository clientRepository;
    private final PermissionRepository permissionRepository;

    @Transactional
    public ClientResponse changeAdminPermission(final ClientNameRequest request) {
        final var client = findClientService.findByName(request.getName());

        final var adminPermission = client.getPermissions().stream()
                .filter(permission -> ADMIN_PERMISSION.equals(permission.getName()))
                .findFirst();

        adminPermission.ifPresentOrElse(permission -> {
            client.getPermissions().remove(permission);
            permissionRepository.delete(permission);
        }, () ->
                client.addPermission(Permission.builder().name(ADMIN_PERMISSION).build()));

        final var result = clientRepository.save(client);

        return ClientMapper.toResponse(result);
    }

}
