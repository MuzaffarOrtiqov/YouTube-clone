package uz.urinov.youtube.mapper;

import uz.urinov.youtube.entity.AttachEntity;
import uz.urinov.youtube.entity.ChannelEntity;

import java.time.LocalDateTime;

public interface VideoShortInfoMapper {

    public String getId();
    public String getTitle();
    public AttachEntity getPreviewAttach();
    public LocalDateTime getPublishedDate();
    public ChannelEntity getChannel();
    public Long getViewCount();




}
