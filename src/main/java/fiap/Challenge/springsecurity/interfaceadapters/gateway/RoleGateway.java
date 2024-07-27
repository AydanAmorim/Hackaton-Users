package fiap.Challenge.springsecurity.interfaceadapters.gateway;

import fiap.Challenge.springsecurity.entities.Role;
import fiap.Challenge.springsecurity.framework.repository.RoleRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleGateway {
    @Resource
    private RoleRepository roleRepository;

    public Optional<Role> findRoleByName(Role.Values role){
        return this.roleRepository.findByType(role);
    }

}
