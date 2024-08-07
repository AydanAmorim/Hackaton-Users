package com.payment.system.authentication_service.interfaceadapters.gateway;


import com.payment.system.authentication_service.entities.User;
import com.payment.system.authentication_service.framework.db.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserGateway {

    private final UserRepository repository;

    public UserGateway(UserRepository repository) {
        this.repository = repository;
    }

    public Page<User> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Optional<User> findUserByUsername(String username) {
        return repository.findUserByUsername(username);
    }

    public void insert(User user) {
        repository.save(user);
    }

    public Optional<User> findByClientId(Integer clientId) {
        return repository.findByClientId(clientId);
    }
}
