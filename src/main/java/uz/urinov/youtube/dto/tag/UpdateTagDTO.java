package uz.urinov.youtube.dto.tag;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTagDTO {
    @NotBlank(message = "Name is required")
    private String name;
}
