package uz.urinov.youtube.dto.auth;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @NonNull
    @Email
    private String username;

    @NonNull
    private String password;
}
