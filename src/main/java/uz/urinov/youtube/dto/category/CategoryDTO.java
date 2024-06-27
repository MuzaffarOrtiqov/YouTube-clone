package uz.urinov.youtube.dto.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDTO {
    /* id,name,created_date*/
    private Integer id;
    @NotBlank(message = "Category name is required")
    @Size(min = 3 ,max = 30 , message = "At least 3 characters are required")
    private String name;
    private LocalDateTime createdDate;
}
