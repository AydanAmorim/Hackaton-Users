package com.payment.system.authentication_service.interfaceadapters.presenters;

import com.payment.system.authentication_service.entities.User;
import com.payment.system.authentication_service.interfaceadapters.presenters.dto.UserDto;
import com.payment.system.authentication_service.util.pagination.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserPresenter {

    public UserDto convert(User user) {
        return new UserDto(
                user.getClientId(),
                user.getRole(),
                user.getUsername()
        );
    }

    public PagedResponse<UserDto> convert(Page<User> page) {
        List<UserDto> userDtos = page.getContent().stream()
                .map(this::convert)
                .toList();

        return new PagedResponse<>(
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                userDtos
        );
    }
}
