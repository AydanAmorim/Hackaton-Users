package fiap.Challenge.springsecurity.util.config.authentication;

import fiap.Challenge.springsecurity.entities.User;
import fiap.Challenge.springsecurity.framework.repository.UserRepository;
import fiap.Challenge.springsecurity.interfaceadapters.controllers.UserController;
import fiap.Challenge.springsecurity.interfaceadapters.presenters.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Configuration
public class AdminUserConfig implements CommandLineRunner {
    @Value("${SYSTEM_DEFAULT_USERNAME:adj2}")
    private String DEFAULT_USERNAME;

    @Value("${SYSTEM_DEFAULT_PASSWORD:dj@1234}")
    private String DEFAULT_PASSWORD;

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final UserController userController;

    public AdminUserConfig(UserRepository userRepository, BCryptPasswordEncoder passEncoder, UserController userController) {
        this.userRepository = userRepository;
        this.passwordEncoder = passEncoder;
        this.userController = userController;
    }

    @Override
    @Transactional
    public void run(String... args){
        System.out.println("validando existencia do usuário padrão: "+ DEFAULT_USERNAME);

        Optional<User> userAdmin = userRepository.findUserByUsername(DEFAULT_USERNAME);

        if (userAdmin.isEmpty()) {
            System.out.println("Usuário padrão não localizado, cadastrando");

            UserDto userDto = new UserDto();
            userDto.setUsername(DEFAULT_USERNAME);
            userDto.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));

            this.userController.insertAdminUser(userDto);

            System.out.println("Usuário padrão cadastrado");
        }

        System.out.println("***************** - Usuário padrão existente - ****************");
    }
}
