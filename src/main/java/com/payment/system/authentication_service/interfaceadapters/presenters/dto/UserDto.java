package com.payment.system.authentication_service.interfaceadapters.presenters.dto;

import com.payment.system.authentication_service.util.enums.Role;

public record UserDto(Integer clientId, Role role, String username) {

}
