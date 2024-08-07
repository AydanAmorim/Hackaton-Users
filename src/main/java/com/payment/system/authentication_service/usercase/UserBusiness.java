package com.payment.system.authentication_service.usercase;

import com.payment.system.authentication_service.entities.User;
import com.payment.system.authentication_service.interfaceadapters.presenters.login.LoginDto;
import com.payment.system.authentication_service.util.enums.Role;
import com.payment.system.authentication_service.util.exceptions.BusinessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserBusiness {

    private final BCryptPasswordEncoder cryptPasswordEncoder;

    public UserBusiness(BCryptPasswordEncoder cryptPasswordEncoder) {
        this.cryptPasswordEncoder = cryptPasswordEncoder;
    }

    public void validateUserCreation(Optional<User> optionalClient, Optional<User> optionalUsername) throws BusinessException {
        if (optionalUsername.isPresent()) {
            throw new BusinessException("USERNAME_ALREADY_EXISTS");
        }

        if (optionalClient.isPresent()) {
            throw new BusinessException("CLIENT_ALREADY_HAS_USERNAME");
        }
    }

    public User createUserInformation(Integer clientId, LoginDto loginDto, Role role) {
        return User.builder()
                .clientId(clientId)
                .role(role)
                .username(loginDto.username())
                .password(cryptPasswordEncoder.encode(loginDto.password()))
                .build();
    }
}
