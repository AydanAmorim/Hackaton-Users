package com.payment.system.authentication_service.unit.interfaceadapters.presenters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.payment.system.authentication_service.TestUtils;
import com.payment.system.authentication_service.entities.User;
import com.payment.system.authentication_service.interfaceadapters.presenters.UserPresenter;
import com.payment.system.authentication_service.interfaceadapters.presenters.dto.UserDto;
import com.payment.system.authentication_service.util.enums.Role;
import com.payment.system.authentication_service.util.pagination.PagedResponse;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

class UserPresenterTest extends TestUtils {

    @Autowired
    private UserPresenter presenter;

    @Test
    void convertEntityToDto() throws JsonProcessingException, JSONException {
        User user = User.builder()
                .username("admin")
                .clientId(1)
                .password("password")
                .role(Role.ADMIN)
                .build();

        UserDto expected = new UserDto(1, Role.ADMIN, "admin");

        UserDto shouldBe = presenter.convert(user);

        super.assertJsonEquals(
                super.objectMapper.writeValueAsString(expected),
                super.objectMapper.writeValueAsString(shouldBe)
        );
    }

    @Test
    void testConvert() throws JsonProcessingException, JSONException {
        User user = User.builder()
                .username("admin")
                .clientId(1)
                .password("password")
                .role(Role.ADMIN)
                .build();

        Page<User> page = new PageImpl<>(List.of(user), PageRequest.of(0, 15), 1);

        PagedResponse<UserDto> expected = new PagedResponse<>(0,
                15,
                1,
                List.of(new UserDto(1, Role.ADMIN, "admin")));

        PagedResponse<UserDto> shouldBe = presenter.convert(page);

        super.assertJsonEquals(
                super.objectMapper.writeValueAsString(expected),
                super.objectMapper.writeValueAsString(shouldBe)
        );
    }
}