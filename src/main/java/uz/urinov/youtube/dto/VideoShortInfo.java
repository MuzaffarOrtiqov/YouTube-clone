package uz.urinov.youtube.dto;

import lombok.Data;

@Data
public class VideoShortInfo {
//    (id,name,channel(id,name),duration)
    private String  id;
    private String  title;
    private ChannelShortInfo channelShortInfo;
}

