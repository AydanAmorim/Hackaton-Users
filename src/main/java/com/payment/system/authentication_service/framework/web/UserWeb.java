package com.payment.system.authentication_service.framework.web;

import com.payment.system.authentication_service.interfaceadapters.controllers.UserController;
import com.payment.system.authentication_service.interfaceadapters.presenters.dto.UserDto;
import com.payment.system.authentication_service.interfaceadapters.presenters.login.LoginDto;
import com.payment.system.authentication_service.util.enums.Role;
import com.payment.system.authentication_service.util.exceptions.BusinessException;
import com.payment.system.authentication_service.util.exceptions.ExternalInterfaceException;
import com.payment.system.authentication_service.util.pagination.PagedResponse;
import com.payment.system.authentication_service.util.pagination.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/usuarios")
@Tag(name = "Usuários")
public class UserWeb {

    @Resource
    private UserController controller;

    @Operation(summary = "Consultar cadastro de usuários")
    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<PagedResponse<UserDto>> findAll(@Parameter(description = "Valor padrão 10. Valor máximo 1000", example = "10") @RequestParam(required = false) Integer pageSize,
                                                          @Parameter(description = "valor padrão 0", example = "0") @RequestParam(required = false) Integer initialPage) {
        return ResponseEntity.ok(controller.findAll(new Pagination(initialPage, pageSize)));
    }

    @Operation(summary = "Adicionar um novo usuário")
    @PostMapping(value = "/basic/{clientId}")
    public ResponseEntity<Void> addUser(@RequestBody LoginDto loginDto, @PathVariable Integer clientId) throws BusinessException, ExternalInterfaceException {
        controller.insert(loginDto, clientId, Role.BASIC);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Adicionar um novo usuário administrador")
    @PostMapping(value = "/admin/{clientId}")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<Void> addAdminUser(@RequestBody LoginDto loginDto, @PathVariable Integer clientId) throws BusinessException, ExternalInterfaceException {
        controller.insert(loginDto, clientId, Role.ADMIN);

        return ResponseEntity.ok().build();
    }
}
