package fiap.Challenge.springsecurity.interfaceadapters.controllers;

import fiap.Challenge.springsecurity.entities.Role;
import fiap.Challenge.springsecurity.entities.User;
import fiap.Challenge.springsecurity.interfaceadapters.gateway.RoleGateway;
import fiap.Challenge.springsecurity.interfaceadapters.gateway.UserGateway;
import fiap.Challenge.springsecurity.interfaceadapters.presenters.UserPresenter;
import fiap.Challenge.springsecurity.interfaceadapters.presenters.dto.UserDto;
import fiap.Challenge.springsecurity.util.pagination.PagedResponse;
import fiap.Challenge.springsecurity.util.pagination.Pagination;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;

@Component
public class UserController {
    @Resource
    private RoleGateway roleGateway;

    @Resource
    private UserGateway userGateway;

    @Resource
    private UserPresenter userPresenter;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    public PagedResponse<UserDto> findAll(Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getPageSize());

        Page<User> users = this.userGateway.findAll(pageable);

        return this.userPresenter.convert(users);
    }

    public UserDto insertAdminUser(UserDto userDto) {
        var adminRole = getRole(Role.Values.ADMIN);

        return insertUser(userDto, adminRole == null ? new Role(Role.Values.ADMIN) : adminRole);
    }

    public UserDto insertBasicUser(UserDto userDto) {
        var basicRole = getRole(Role.Values.BASIC);

        return insertUser(userDto, basicRole == null ? new Role(Role.Values.BASIC) : basicRole);
    }

    private UserDto insertUser(UserDto userDto, Role role) {
        this.create(userDto);

        User user = this.userPresenter.convert(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user.setRoles(Set.of(role));
        user = this.userGateway.insert(user);

        return this.userPresenter.convert(user);
    }

    private Role getRole(Role.Values userType) {
        Optional<Role> role = roleGateway.findRoleByName(userType);

        return role.orElse(null);
    }

    private void create(UserDto userDto) {
        Optional<User> userCreate = this.userGateway.findUserByUsername(userDto.getUsername());

        if (userCreate.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

}
