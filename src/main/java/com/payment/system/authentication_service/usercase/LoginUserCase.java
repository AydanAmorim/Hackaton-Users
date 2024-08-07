package com.payment.system.authentication_service.usercase;

import com.payment.system.authentication_service.entities.User;
import com.payment.system.authentication_service.interfaceadapters.presenters.login.LoginDto;
import com.payment.system.authentication_service.util.MessageUtil;
import com.payment.system.authentication_service.util.exceptions.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class LoginUserCase {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtEncoder jwtEncoder;

    @Value("${time.to.token.expire}")
    private Long expiresTokenInSeconds;

    @Value("${token.authority}")
    private String tokenAuthority;

    public LoginUserCase(BCryptPasswordEncoder bCryptPasswordEncoder, JwtEncoder jwtEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    public void validateLogin(Optional<User> user, LoginDto loginInformation) throws BusinessException {
        if (user.isEmpty()) {
            throw new BusinessException("USERNAME_NOT_FOUND");
        }

        if (!bCryptPasswordEncoder.matches(loginInformation.password(), user.get().getPassword())) {
            throw new BadCredentialsException(MessageUtil.getMessage("ERROR_ON_LOGIN_INFORMATION"));
        }
    }

    public String createToken(User user) {
        Instant creationDate = Instant.now();
        Instant expiredDateTime = creationDate.plusSeconds(expiresTokenInSeconds);

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer(tokenAuthority)
                .subject(user.getClientId().toString())
                .issuedAt(creationDate)
                .expiresAt(expiredDateTime)
                .claim("scope", user.getRole())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
}
