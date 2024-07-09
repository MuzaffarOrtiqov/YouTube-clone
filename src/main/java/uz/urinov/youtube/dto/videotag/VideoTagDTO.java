package uz.urinov.youtube.dto.videotag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class VideoTagDTO {
    /*  id,video_id,tag_id,created_date*/
    private String id;
    @NotBlank(message = "Video id is required")
    private String videoId;
    @NotNull(message = "Tag is required")
    private Integer tagId;
    private LocalDateTime createdDate;

  //  private List<Integer> tagIdList;
}
