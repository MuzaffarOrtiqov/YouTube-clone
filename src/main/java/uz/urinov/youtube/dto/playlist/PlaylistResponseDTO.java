package uz.urinov.youtube.dto.playlist;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.youtube.dto.channel.ChannelResponseDTO;
import uz.urinov.youtube.dto.profile.ProfileResponseDTO;
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
//    private String channelId;
//    private String channelName;
//    private Integer profileId;
    private ProfileResponseDTO profile;
    private ChannelResponseDTO channel;
    private LocalDateTime created;

}
