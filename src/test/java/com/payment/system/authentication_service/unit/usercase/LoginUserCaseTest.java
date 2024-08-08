package com.payment.system.authentication_service.unit.usercase;

import com.payment.system.authentication_service.TestUtils;
import com.payment.system.authentication_service.entities.User;
import com.payment.system.authentication_service.interfaceadapters.presenters.login.LoginDto;
import com.payment.system.authentication_service.usercase.LoginUserCase;
import com.payment.system.authentication_service.util.MessageUtil;
import com.payment.system.authentication_service.util.enums.Role;
import com.payment.system.authentication_service.util.exceptions.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoginUserCaseTest extends TestUtils {

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private JwtEncoder jwtEncoder;

    @Autowired
    private LoginUserCase loginUserCase;

    @Test
    @DisplayName("Validar login usuário não encontrado")
    void validateLoginUserNotFoundThrowsBusinessException() {
        Optional<User> user = Optional.empty();
        LoginDto loginInformation = new LoginDto("user", "password");

        BusinessException exception = assertThrows(BusinessException.class, () -> loginUserCase.validateLogin(user, loginInformation));

        assertEquals(MessageUtil.getMessage("USERNAME_NOT_FOUND"), exception.getMessage());
    }

    @Test
    @DisplayName("Validar login credenciar diferentes")
    void validateLoginInvalidPasswordThrowsBadCredentialsException() {
        User user = new User();
        user.setPassword("hashedPassword");
        Optional<User> optionalUser = Optional.of(user);

        LoginDto loginInformation = new LoginDto("user", "wrongPassword");

        when(bCryptPasswordEncoder.matches(any(CharSequence.class), any(String.class)))
                .thenReturn(false);

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> loginUserCase.validateLogin(optionalUser, loginInformation));

        assertEquals(MessageUtil.getMessage("ERROR_ON_LOGIN_INFORMATION"), exception.getMessage());
    }

    @Test
    @DisplayName("Validar login")
    void validateLoginValidCredentials() {
        User user = new User();
        user.setPassword("correctPassword");
        Optional<User> optionalUser = Optional.of(user);
        LoginDto loginInformation = new LoginDto("user", "correctPassword");

        when(bCryptPasswordEncoder.matches(any(CharSequence.class), any(String.class)))
                .thenReturn(true);

        assertDoesNotThrow(() -> loginUserCase.validateLogin(optionalUser, loginInformation));
    }

    @Test
    @DisplayName("Criar token")
    void createTokenValidUserReturnsJwtToken() {
        User user = new User();
        user.setClientId(1);
        user.setRole(Role.ADMIN);

        Jwt expectedJwt = mock(Jwt.class);
        when(expectedJwt.getTokenValue()).thenReturn("mockedTokenValue");
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(expectedJwt);
        ArgumentCaptor<JwtEncoderParameters> captor = ArgumentCaptor.forClass(JwtEncoderParameters.class);

        String token = loginUserCase.createToken(user);

        verify(jwtEncoder).encode(captor.capture());

        JwtClaimsSet claimsSet = captor.getValue().getClaims();
        assertEquals("authentication-service-test", claimsSet.getClaims().get("iss"));
        assertEquals("1", claimsSet.getClaims().get("sub"));
        assertEquals(Role.ADMIN, claimsSet.getClaims().get("scope"));

        Instant now = Instant.now();
        assertTrue(claimsSet.getIssuedAt().isBefore(now.plusSeconds(1)));
        assertTrue(claimsSet.getExpiresAt().isBefore(now.plusSeconds(3601)));

        assertEquals("mockedTokenValue", token);
    }
}