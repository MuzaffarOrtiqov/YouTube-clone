package uz.urinov.youtube.dto.tag;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TagDTO {
    /*  id,name,created_date*/
    private Integer id;
    @NotBlank(message = "Category name is required")
    private String name;
    private LocalDateTime createdDate;
}
