package uz.urinov.youtube.dto.playlist;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.youtube.dto.attach.AttachDTO;
import uz.urinov.youtube.dto.channel.ChannelResponseDTO;
import uz.urinov.youtube.dto.profile.ProfileResponseDTO;
import uz.urinov.youtube.dto.video.VideoDTO;
import uz.urinov.youtube.enums.PlaylistStatus;

import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistResponseDTO {
    private Integer id;
    private Integer orderNum;
    private String name;
    private String description;
    private PlaylistStatus status;
    private Integer videoCount;
    private ProfileResponseDTO profile;
    private ChannelResponseDTO channel;
    private LocalDateTime created;
    private VideoDTO videoDTO;
    private AttachDTO attachDTO;
    private Long  playlistTotalViewCount;

}
