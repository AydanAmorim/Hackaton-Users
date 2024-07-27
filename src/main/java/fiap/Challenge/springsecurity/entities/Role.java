package fiap.Challenge.springsecurity.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Integer Id;

    @Enumerated(EnumType.STRING)
    private Values type;

    public Role(Values role) {
        this.type = role;
    }

    public String getNameAsString(){
        if (this.type == null) {
            return "";
        }

        return type.name();
    }

    public enum Values {
        ADMIN,
        BASIC
    }


}
