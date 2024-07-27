package fiap.Challenge.springsecurity.interfaceadapters.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fiap.Challenge.springsecurity.entities.Role;
import fiap.Challenge.springsecurity.interfaceadapters.gateway.UserGateway;
import fiap.Challenge.springsecurity.interfaceadapters.presenters.login.LoginRequest;
import fiap.Challenge.springsecurity.interfaceadapters.presenters.login.LoginResponse;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;

@Component
public class JwtController {
    @Value("${SYSTEM_DEFAULT_TIME_EXPIRE_TOKEN}")
    private Long expiresTokenInSeconds;

    @Value("${spring.application.name}")
    private String DEFAULT_SYSNAME;

    @Resource
    private UserGateway userGateway;

    @Resource
    private JwtEncoder jwtEncoder;

    @Resource
    private JwtDecoder decoder;

    @Resource
    private ObjectMapper mapper;

    public LoginResponse createTokenJWT(LoginRequest loginRequest) {
        var user = this.userGateway.findUserByUsername(loginRequest.username());

        Instant actualDateTime = Instant.now();
        Instant expiredDateTime = actualDateTime.plusSeconds(expiresTokenInSeconds);

        var scopes = user.get().getRoles()
                .stream()
                .map(Role::getNameAsString)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer(this.DEFAULT_SYSNAME)
                .subject(user.get().getId().toString())
                .issuedAt(actualDateTime)
                .expiresAt(expiredDateTime)
                .claim("scope", scopes)
                .build();

        var jwtValue = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return new LoginResponse(jwtValue, LocalDateTime.ofInstant(expiredDateTime, ZoneId.of("America/Sao_Paulo")).toString());

    }

    public JsonNode validateToken(String substring) {
        Jwt jwt = decoder.decode(substring);

        ObjectNode json = mapper.createObjectNode();
        json.put("scope", jwt.getClaims().get("scope").toString());
        json.put("expiration", LocalDateTime.ofInstant(
                        Instant.parse(jwt.getClaims().get("exp").toString()),
                        ZoneId.of("America/Sao_Paulo"))
                .toString());

        return json;
    }


}
