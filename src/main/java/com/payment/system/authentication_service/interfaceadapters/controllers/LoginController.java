package com.payment.system.authentication_service.interfaceadapters.controllers;

import com.payment.system.authentication_service.entities.User;
import com.payment.system.authentication_service.interfaceadapters.gateway.UserGateway;
import com.payment.system.authentication_service.interfaceadapters.presenters.login.LoginDto;
import com.payment.system.authentication_service.interfaceadapters.presenters.login.LoginResponse;
import com.payment.system.authentication_service.usercase.LoginUserCase;
import com.payment.system.authentication_service.util.exceptions.BusinessException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LoginController {

    private final LoginUserCase userCase;

    private final UserGateway gateway;

    public LoginController(LoginUserCase userCase, UserGateway gateway) {
        this.userCase = userCase;
        this.gateway = gateway;
    }

    public LoginResponse create(LoginDto loginInformation) throws BusinessException {
        Optional<User> user = gateway.findUserByUsername(loginInformation.username());

        userCase.validateLogin(user, loginInformation);

        String token = userCase.createToken(user.get());

        return new LoginResponse(token);
    }
}
