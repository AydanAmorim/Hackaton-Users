package com.payment.system.authentication_service.interfaceadapters.presenters.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Campos para efetuar login no sistema")
public record LoginDto(
        @Schema(description = "Username", example = "adj2")
        @Pattern(regexp = "(\\w)+(\\.)?(\\w)+$")
        @JsonProperty(value = "usuario")
        @NotEmpty
        String username,

        @Schema(description = "Password", example = "adj@1234")
        @JsonProperty(value = "senha")
        @NotEmpty
        String password) {
}