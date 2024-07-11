package uz.urinov.youtube.mapper;

import java.time.LocalDateTime;

public interface CommentInfoMapper {
    Integer getId();

    String getContent();

    LocalDateTime getCreatedDate();

    Integer getLikeCount();

    Integer getDisLikeCount();

    Integer getProfileId();

    String getName();

    String getSurname();

    String getPhotoId();
}
