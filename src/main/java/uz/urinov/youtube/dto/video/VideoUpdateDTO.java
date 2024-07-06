package uz.urinov.youtube.dto.video;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.youtube.enums.VideoStatus;
import uz.urinov.youtube.enums.VideoType;
@Getter
@Setter
public class VideoUpdateDTO {

    private String  title;
    private Integer categoryId;
    private String  description;

}
