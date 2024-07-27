package fiap.Challenge.springsecurity.interfaceadapters.web;
import fiap.Challenge.springsecurity.interfaceadapters.controllers.UserController;
import fiap.Challenge.springsecurity.interfaceadapters.presenters.dto.UserDto;
import fiap.Challenge.springsecurity.util.pagination.PagedResponse;
import fiap.Challenge.springsecurity.util.pagination.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value="/api/usuarios")
@Tag(name ="Gerenciamento do cadastro de usuários")
public class UserWeb {
    @Resource
    private UserController userController;

    @Operation(summary = "Consultar cadastro de usuários (Somente administrador pode acessar)")
    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<PagedResponse<UserDto>> findAll(@Parameter(description = "Valor padrão 10. Valor máximo 1000", example = "10") @RequestParam(required = false) Integer pageSize,
                                                          @Parameter(description = "valor padrão 0", example = "0") @RequestParam(required = false) Integer initialPage) {

        Pagination page = new Pagination(initialPage, pageSize);
        return ResponseEntity.ok(this.userController.findAll(page));
    }

    @Operation(summary="Adicionar um novo usuário")
    @PostMapping("adicionar_usuario_basico")
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        UserDto userSavedDto = this.userController.insertBasicUser(userDto);
        return ResponseEntity.ok(userSavedDto);
    }

    @Operation(summary="Adicionar um novo usuário Administrador (Somente administrador pode acessar)")
    @PostMapping("adicionar_usuario_admin")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<UserDto> addAdminUser(@RequestBody UserDto userDto) {
        UserDto userSavedDto = this.userController.insertAdminUser(userDto);
        return ResponseEntity.ok(userSavedDto);
    }

}
