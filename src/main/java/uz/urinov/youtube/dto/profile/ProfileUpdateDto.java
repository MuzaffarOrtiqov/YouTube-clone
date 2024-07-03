package uz.urinov.youtube.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateDto {

    private String name;

    private String surname;
}
