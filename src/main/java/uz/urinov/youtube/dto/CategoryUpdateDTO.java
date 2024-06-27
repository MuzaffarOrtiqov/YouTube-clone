package uz.urinov.youtube.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryUpdateDTO {
    @NotBlank(message = "Category name is required")
    @Size(min = 3 ,max = 30 , message = "At least 3 characters are required")
    private String name;
}
