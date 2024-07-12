package uz.urinov.youtube.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uz.urinov.youtube.entity.VideoLikeEntity;

import java.util.List;
import java.util.Optional;

public interface VideoLikeRepository extends CrudRepository<VideoLikeEntity,Long> {
    @Query("FROM VideoLikeEntity AS vl WHERE vl.profileId=?1 AND vl.videoId=?2")
    Optional<VideoLikeEntity> hasLikedTheVideo(Integer profileId, String videoId);


    @Query("SELECT vl.id as id, v.id as videoId, " +
            "v.channelId as channelId, " +
            "v.previewAttachId as previewAttachId " +
            "FROM VideoLikeEntity AS vl " +
            "INNER JOIN VideoEntity AS v ON vl.videoId = v.id " +
            "INNER JOIN ChannelEntity AS c ON v.channelId = c.id " +
            "INNER JOIN AttachEntity AS a ON a.id = v.attachId " +
            "INNER JOIN ProfileEntity AS p ON p.id = c.profileId " +
            "WHERE v.visible = true AND p.id = ?1 " +
            "ORDER BY vl.createdDate DESC")
    Page<Object[]> getUserLikedVideos(Integer userId, Pageable pageable);



}
