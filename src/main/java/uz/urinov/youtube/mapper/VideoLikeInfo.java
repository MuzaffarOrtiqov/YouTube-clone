package uz.urinov.youtube.mapper;

import lombok.Getter;
import lombok.Setter;
import uz.urinov.youtube.dto.attach.AttachShortInfo;
import uz.urinov.youtube.dto.video.VideoShortInfo;

@Getter
@Setter
public class VideoLikeInfo {
    /*  id,video(id,name,channel(id,name),duration),preview_attach(id,url)*/
    private Long id;
    private VideoShortInfo videoShortInfo;
    private AttachShortInfo attachShortInfo;
}
