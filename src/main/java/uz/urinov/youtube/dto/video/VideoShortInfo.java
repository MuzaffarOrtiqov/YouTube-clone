package uz.urinov.youtube.dto.video;

import lombok.Data;
import uz.urinov.youtube.dto.channel.ChannelShortInfo;

@Data
public class VideoShortInfo {
//    (id,name,channel(id,name),duration)
    private String  id;
    private String  title;
    private ChannelShortInfo channelShortInfo;
}

