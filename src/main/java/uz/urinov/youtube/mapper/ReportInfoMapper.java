package uz.urinov.youtube.mapper;

import java.time.LocalDateTime;

public interface ReportInfoMapper {
    Integer getId();

    String getContent();

    String getEntityId();

    String getStatus();

    LocalDateTime getCreatedDate();

    Integer getProfileId();

    String getName();

    String getSurname();

    String getPhotoId();
}
