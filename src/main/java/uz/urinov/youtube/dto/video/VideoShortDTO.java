package uz.urinov.youtube.dto.video;

import lombok.Data;
import uz.urinov.youtube.dto.attach.AttachDTO;

@Data
public class VideoShortDTO {
    private String id;
    private String videoTitle;
    private Long duration;
}
