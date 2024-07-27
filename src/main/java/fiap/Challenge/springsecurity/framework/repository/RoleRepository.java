package fiap.Challenge.springsecurity.framework.repository;

import fiap.Challenge.springsecurity.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

        Optional<Role> findByType(Role.Values role);
}