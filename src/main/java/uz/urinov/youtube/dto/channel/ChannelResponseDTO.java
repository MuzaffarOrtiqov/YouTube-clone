package uz.urinov.youtube.dto.channel;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.youtube.enums.ChannelStatus;

import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChannelResponseDTO {

    private String id;
    private String name;
    private String photo;
    private String description;
    private String banner;
    private ChannelStatus status;
    private Integer profileId;
    private LocalDateTime createDate;
}
