package com.payment.system.authentication_service.framework.web;

import com.payment.system.authentication_service.interfaceadapters.controllers.LoginController;
import com.payment.system.authentication_service.interfaceadapters.presenters.login.LoginDto;
import com.payment.system.authentication_service.interfaceadapters.presenters.login.LoginResponse;
import com.payment.system.authentication_service.util.exceptions.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/autenticacao")
@Tag(name = "Autenticação de usuário")
public class TokenWeb {

    @Resource
    private LoginController controller;

    @PostMapping
    @Operation(summary = "Criar token")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginDto body) throws BusinessException {
        return ResponseEntity.ok(this.controller.create(body));
    }
}

