package com.payment.system.authentication_service.interfaceadapters.controllers;

import com.payment.system.authentication_service.entities.User;
import com.payment.system.authentication_service.framework.external.CustomerWeb;
import com.payment.system.authentication_service.interfaceadapters.gateway.UserGateway;
import com.payment.system.authentication_service.interfaceadapters.presenters.UserPresenter;
import com.payment.system.authentication_service.interfaceadapters.presenters.dto.UserDto;
import com.payment.system.authentication_service.interfaceadapters.presenters.login.LoginDto;
import com.payment.system.authentication_service.usercase.UserBusiness;
import com.payment.system.authentication_service.util.enums.Role;
import com.payment.system.authentication_service.util.exceptions.BusinessException;
import com.payment.system.authentication_service.util.exceptions.ExternalInterfaceException;
import com.payment.system.authentication_service.util.pagination.PagedResponse;
import com.payment.system.authentication_service.util.pagination.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserController {

    private final UserGateway gateway;

    private final UserPresenter presenter;

    private final UserBusiness business;

    private final CustomerWeb customerWeb;

    @Autowired
    public UserController(UserGateway gateway, UserPresenter presenter, UserBusiness business, CustomerWeb customerWeb) {
        this.gateway = gateway;
        this.presenter = presenter;
        this.business = business;
        this.customerWeb = customerWeb;
    }

    public PagedResponse<UserDto> findAll(Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getPageSize());

        Page<User> users = gateway.findAll(pageable);

        return presenter.convert(users);
    }

    public void insert(LoginDto loginDto, Integer clientId, Role role) throws BusinessException, ExternalInterfaceException {
        Optional<User> optionalUsername = gateway.findUserByUsername(loginDto.username());
        Optional<User> optionalClient = gateway.findByClientId(clientId);

        business.validateUserCreation(optionalClient, optionalUsername);

        customerWeb.findClientById(clientId);

        User user = business.createUserInformation(clientId, loginDto, role);

        gateway.insert(user);
    }
}
