package br.feevale.security.service;

import br.feevale.security.controller.request.ClientEmailRequest;
import br.feevale.security.controller.response.ClientResponse;
import br.feevale.security.domain.Client;
import br.feevale.security.domain.Permission;
import br.feevale.security.mapper.ClientMapper;
import br.feevale.security.repository.ClientRepository;
import br.feevale.security.repository.PermissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ChangeClientPermissionsService {

    private static final String ADMIN_PERMISSION = "ADMIN";
    private FindClientService findClientService;
    private ClientRepository clientRepository;
    private PermissionRepository permissionRepository;


    @Transactional
    public ClientResponse changeAdminPermission(ClientEmailRequest request) {
        Client client = findClientService.findByEmail(request.getEmail());

        Optional<Permission> adminPermission = client.getPermissions().stream()
                .filter(permission -> ADMIN_PERMISSION.equals(permission.getName()))
                .findFirst();

        if (adminPermission.isPresent()) {
            Permission permission = adminPermission.get();
            client.getPermissions().remove(permission);
            permissionRepository.delete(permission);
        } else {
            client.addPermission(
                    Permission.builder().name(ADMIN_PERMISSION).build()
            );
        }

        clientRepository.save(client);

        return ClientMapper.toResponse(client);
    }

}
