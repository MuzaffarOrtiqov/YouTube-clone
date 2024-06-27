package uz.urinov.youtube.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.youtube.enums.ProfileRole;
import uz.urinov.youtube.enums.ProfileStatus;

@Getter
@Setter
public class ProfileCreateDTO {

    @Size(min = 3, max = 50, message = "Berilgan name uzunligi 3 va 50 orasida bo'lishi kerak")
    @NotBlank(message = "name bo'sh bo'lishi mumkin emas")
    private String name;

    @Size(min = 3, max = 50, message = "Berilgan surname uzunligi 3 va 50 orasida bo'lishi kerak")
    @NotBlank(message = "Surname bo'sh bo'lishi mumkin emas")
    private String surname;

    @Email
    @Size(min = 3, max = 50, message = "Berilgan email uzunligi 3 va 50 orasida bo'lishi kerak")
    @NotBlank(message = "Email bo'sh bo'lishi mumkin emas")
    private String email;

    @Size(min = 4, max = 50, message = "Berilgan password uzunligi 3 va 50 orasida bo'lishi kerak")
    @NotBlank(message = "Password bo'sh bo'lishi mumkin emas")
    private String password;

    private ProfileStatus status;
    private ProfileRole role;

}
