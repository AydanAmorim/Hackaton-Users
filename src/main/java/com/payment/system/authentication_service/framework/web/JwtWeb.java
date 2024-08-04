package com.payment.system.authentication_service.framework.web;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.interfaces.RSAPublicKey;
import java.util.Map;

@RestController
@RequestMapping(value = "/key")
@Tag(name = "Validação de Token", description = "Endpoint para validação da chave via Gateway")
public class JwtWeb {

    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;

    @GetMapping(value = "/.well-known/jwks.json")
    public Map<String, Object> getPublicKey() {
        RSAKey key = new RSAKey.Builder(publicKey).build();

        return new JWKSet(key).toJSONObject();
    }
}
