package uz.urinov.youtube.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uz.urinov.youtube.dto.videotag.VideoTagShortDto;
import uz.urinov.youtube.entity.VideoTagEntity;

import java.util.List;
import java.util.Optional;

public interface VideoTagRepository extends CrudRepository<VideoTagEntity, String> {
    @Query(value = "SELECT vt.tagId FROM VideoTagEntity AS vt WHERE vt.videoId=?1 ")
    List<Integer> findTagIdListByVideoId(String videoId);

    @Query(value = "select tag.id as id, tag.name as name from video_tag inner join  tag on tag.id = video_tag.tag_id where\n" +
            " video_id = ?1", nativeQuery = true)
    List<VideoTagShortDto> findAllByVideoId(String videoId);

    Optional<VideoTagEntity> findByTagId(Integer tagId);


    @Modifying
    @Transactional
    @Query("DELETE FROM VideoTagEntity AS vt WHERE vt.videoId=?1 AND vt.tagId=?2")
    void delete(String videoId, Integer tagId);

    @Query(value = "FROM VideoTagEntity AS vt WHERE vt.videoId=?1 ")
    List<VideoTagEntity> findVideoTagEntitiesBy(String videoId);

    @Query("SELECT p.id FROM VideoTagEntity AS vt " +
            "INNER JOIN VideoEntity AS v ON vt.videoId=v.id " +
            "INNER JOIN ChannelEntity AS c ON v.channelId=c.id " +
            "INNER JOIN ProfileEntity AS p ON c.profileId =p.id " +
            "WHERE v.visible=true ")
    Integer findUser(String videoId);
}
