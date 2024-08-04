package com.payment.system.authentication_service.framework.db;

import com.payment.system.authentication_service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserByUsername(String name);

    Optional<User> findByClientId(Integer clientId);
}
