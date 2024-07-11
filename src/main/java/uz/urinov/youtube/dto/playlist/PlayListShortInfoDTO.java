package uz.urinov.youtube.dto.playlist;

import lombok.Data;
import uz.urinov.youtube.dto.video.VideoShortDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PlayListShortInfoDTO {
    private Integer id;
    private String name;
    private Integer videoCount;
    private LocalDateTime created;

    private ChannelShortInfoDTO channel;
    private List<VideoShortDTO> videoList;
}
