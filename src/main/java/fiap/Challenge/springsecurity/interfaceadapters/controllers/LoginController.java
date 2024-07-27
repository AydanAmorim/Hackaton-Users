package fiap.Challenge.springsecurity.interfaceadapters.controllers;

import fiap.Challenge.springsecurity.interfaceadapters.gateway.UserGateway;
import fiap.Challenge.springsecurity.interfaceadapters.presenters.login.LoginRequest;
import fiap.Challenge.springsecurity.interfaceadapters.presenters.login.LoginResponse;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class LoginController {
    @Resource
    private UserGateway userGateway;
    @Resource
    private JwtController jwtController;
    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    public LoginResponse create(LoginRequest loginRequest) {
        validateLogin(loginRequest);

        return this.jwtController.createTokenJWT(loginRequest);
    }

    private void validateLogin(LoginRequest loginRequest){
        var user = this.userGateway.findUserByUsername(loginRequest.username());

        if (user.isEmpty() || !user.get().isLoginCorrect(loginRequest.password(), this.passwordEncoder)) {
            throw new BadCredentialsException("Usuário ou senha são inválidos, verifique suas credenciais!");
        }
    }


}
