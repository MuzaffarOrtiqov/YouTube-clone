package uz.urinov.youtube.mapper;

import java.time.LocalDateTime;

public interface PlayListInfoMapper {
    Integer getPlaylistId();

    String getPlaylistName();

    String getChannelId();

    String getChannelName();

    LocalDateTime getPlaylistCreated();

}
