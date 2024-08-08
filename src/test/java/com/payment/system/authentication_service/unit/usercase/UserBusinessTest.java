package com.payment.system.authentication_service.unit.usercase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.payment.system.authentication_service.TestUtils;
import com.payment.system.authentication_service.entities.User;
import com.payment.system.authentication_service.interfaceadapters.presenters.login.LoginDto;
import com.payment.system.authentication_service.usercase.UserBusiness;
import com.payment.system.authentication_service.util.MessageUtil;
import com.payment.system.authentication_service.util.enums.Role;
import com.payment.system.authentication_service.util.exceptions.BusinessException;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserBusinessTest extends TestUtils {

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserBusiness userBusiness;

    @DisplayName("Validar criar usuário que já existe")
    @Test
    void validateUserThrowsExceptionAlreadyExists() {
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> userBusiness.validateUserCreation(Optional.empty(), Optional.of(new User()))
        );

        assertEquals(MessageUtil.getMessage("USERNAME_ALREADY_EXISTS"), exception.getMessage());
    }

    @DisplayName("Validar criar usuário já cadastrado")
    @Test
    void validateUserThrowsExceptionAlreadyRegistered() {
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> userBusiness.validateUserCreation(Optional.of(new User()), Optional.empty())
        );

        assertEquals(MessageUtil.getMessage("CLIENT_ALREADY_HAS_USERNAME"), exception.getMessage());
    }

    @DisplayName("Validar criar usuário")
    @Test
    void validateUserDoesNotThrow() {
        assertDoesNotThrow(() ->
                userBusiness.validateUserCreation(Optional.empty(), Optional.empty())
        );
    }

    @DisplayName("Criar usuário")
    @Test
    void createUser() throws JsonProcessingException, JSONException {
        when(bCryptPasswordEncoder.encode(any(String.class)))
                .thenReturn("password");

        User expected = User.builder()
                .clientId(1)
                .role(Role.ADMIN)
                .username("admin")
                .password("password")
                .build();

        User shouldBe = userBusiness.createUserInformation(
                1,
                new LoginDto("admin", "senha1123"),
                Role.ADMIN
        );

        super.assertJsonEquals(
                super.objectMapper.writeValueAsString(expected),
                super.objectMapper.writeValueAsString(shouldBe));
    }
}