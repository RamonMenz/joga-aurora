package br.feevale.security.controller;

import br.feevale.security.controller.request.ClientEmailRequest;
import br.feevale.security.controller.request.ClientRequest;
import br.feevale.security.controller.request.UpdateClientRequest;
import br.feevale.security.controller.response.ClientResponse;
import br.feevale.security.service.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@AllArgsConstructor
@RestController
@RequestMapping("/client")
public class ClientController {

    private AddClientService addClientService;
    private UpdateClientService updateClientService;
    private RemoveClientService removeClientService;
    private ActivateClientService activateClientService;
    private ChangeClientPermissionsService changeClientPermissionsService;
    private FindClientService findClientService;

    @PostMapping
    public ClientResponse add(@Valid @RequestBody ClientRequest request) {
        return addClientService.add(request);
    }

    @PutMapping
    public ClientResponse update(@Valid @RequestBody UpdateClientRequest request) {
        return updateClientService.update(request);
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    public void remove() {
        removeClientService.remove();
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/change-active-status")
    public ClientResponse changeActiveStatus(@Valid @RequestBody ClientEmailRequest request) {
        return activateClientService.changeActiveStatus(request);
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/change-admin-permission")
    public ClientResponse changeAdminPermission(@Valid @RequestBody ClientEmailRequest request) {
        return changeClientPermissionsService.changeAdminPermission(request);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping
    public Page<ClientResponse> findAll(@RequestParam(required = false, defaultValue = "") String text,
                                        @RequestParam(required = false, defaultValue = "false") boolean inactiveOnly,
                                        Pageable pageable) {
        return findClientService.findAllByName(text, inactiveOnly, pageable);
    }

    @GetMapping("/me")
    public ClientResponse find() {
        return findClientService.find();
    }
}
