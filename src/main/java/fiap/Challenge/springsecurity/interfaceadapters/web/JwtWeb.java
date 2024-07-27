package fiap.Challenge.springsecurity.interfaceadapters.web;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.interfaces.RSAPublicKey;
import java.util.Map;

@RequestMapping(value = "/key")
public class JwtWeb {
    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;

    @GetMapping( "${jwt.public.key}")
    public Map<String, Object> getPublicKey() {
        RSAKey key = new RSAKey.Builder(publicKey).build();

        return new JWKSet(key).toJSONObject();
    }
}
