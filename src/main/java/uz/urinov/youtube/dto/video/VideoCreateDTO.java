package uz.urinov.youtube.dto.video;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.youtube.dto.tag.TagDTO;
import uz.urinov.youtube.enums.VideoType;

import java.util.List;

@Getter
@Setter
public class VideoCreateDTO {
    private String  preview_attach_id;
    @NotBlank(message = "Title is required")
    private String  title;
    @NotNull(message = "You must specify the category")
    private Integer categoryId;
    @NotBlank(message = "Attach is required")
    private String attachId;
    @NotNull(message = "Type is required")
    private VideoType type;
    @NotBlank(message = "Channel is required")
    private String channelId;
    private List<Integer> tags;


}
