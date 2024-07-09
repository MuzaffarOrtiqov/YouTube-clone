package uz.urinov.youtube.mapper;

import java.time.LocalDateTime;

public interface PlaylistVideoInfoMapper {

    Integer getPlaylistId();

    Integer getVideoId();

    String getPreviewAttachId();

    String getVideoTitle();

    Long getDuration();

    String getChannelId();

    String getChannelName();

    Integer getOrderNumber();

    LocalDateTime getCreatedDate();

}
