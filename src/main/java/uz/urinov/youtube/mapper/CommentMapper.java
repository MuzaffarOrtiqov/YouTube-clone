package uz.urinov.youtube.mapper;

import java.time.LocalDateTime;

public interface CommentMapper {
    Integer getId();

    String getContent();

    LocalDateTime getCreated_date();

    Integer getLikeCount();

    Integer getDisLike_count();

    String getVideoId();

    String getOrigin_name();

    String getPreview_attach_id();

    String getTitle();

    Long getDuration();

}
