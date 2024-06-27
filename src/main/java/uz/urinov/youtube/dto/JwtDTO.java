package uz.urinov.youtube.dto;

import lombok.Getter;
import lombok.Setter;
import uz.urinov.youtube.enums.ProfileRole;


@Setter
@Getter
public class JwtDTO {
    private Integer id;
    private String username;
    private ProfileRole role;

    public JwtDTO(Integer id) {
        this.id = id;
    }

    public JwtDTO(Integer id, ProfileRole role) {
        this.id = id;
        this.role = role;
    }

    public JwtDTO(Integer id, String username, ProfileRole role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }
}
