package fiap.Challenge.springsecurity.interfaceadapters.presenters.login;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Campos para efetuar login no sistema")
public record LoginRequest(
        @Schema(description = "Username", example = "adj2")
        String username,

        @Schema(description = "Password", example = "adj@1234")
        String password) {
}