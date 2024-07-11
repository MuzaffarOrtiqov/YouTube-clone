package uz.urinov.youtube.mapper;

import lombok.Getter;
import lombok.Setter;
import uz.urinov.youtube.dto.AttachShortInfo;
import uz.urinov.youtube.dto.VideoShortInfo;
import uz.urinov.youtube.dto.attach.AttachDTO;
import uz.urinov.youtube.dto.channel.ChannelResponseDTO;
import uz.urinov.youtube.dto.video.VideoDTO;

@Getter
@Setter
public class VideoLikeInfo {
    /*  id,video(id,name,channel(id,name),duration),preview_attach(id,url)*/
    private Long id;
    private VideoShortInfo videoShortInfo;
    private AttachShortInfo attachShortInfo;
}
