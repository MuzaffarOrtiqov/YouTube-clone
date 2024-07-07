package uz.urinov.youtube.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uz.urinov.youtube.dto.video.VideoDTO;
import uz.urinov.youtube.entity.VideoEntity;
import uz.urinov.youtube.enums.VideoStatus;
import uz.urinov.youtube.mapper.VideoShortInfoMapper;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends CrudRepository<VideoEntity, String> {

    @Query(value = "FROM VideoEntity  AS v WHERE v.attachId=?1")
    Optional<VideoEntity> findByAttachId(String attachId);

    @Query(value = "select c.profile_id from video as v " +
            "inner join channel as c on v.channel_id = c.id " +
            "where v.id=?1 ", nativeQuery = true)
    Integer findUser(String videoId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE VideoEntity AS v SET v.status=:status, v.publishedDate=current_timestamp WHERE v.attachId =:attachId")
    void updateStatus(String attachId, VideoStatus status);

    @Transactional
    @Modifying
    @Query(value = "UPDATE VideoEntity AS v SET v.viewCount = COALESCE(v.viewCount ,0)+1 WHERE v.attachId=?1")
    void increaseViewCount(String attachId);

   /*id,title, preview_attach(id,url),
                   published_date, channel(id,name,photo(url)),
                   view_count,duration*/
    @Query(value ="SELECT v FROM VideoEntity AS v " +
            "INNER JOIN  AttachEntity AS a ON v.attachId=a.id " +
            "INNER JOIN ChannelEntity AS c ON v.channelId=c.id " +
            "WHERE v.categoryId=?1")
    Page<VideoEntity> pagination(Integer categoryId, Pageable pageable);

    @Query(value = "FROM VideoEntity AS v WHERE v.title LIKE %?1%")
    List<VideoEntity> findByTile(String title);

    @Query(value = "FROM VideoEntity AS v INNER JOIN VideoTagEntity AS vt ON v.id = vt.videoId WHERE vt.tagId=?1" )
    Page<VideoEntity> paginationWithTagId(Integer tagId, Pageable pageable);

    @Query(value = "FROM VideoEntity AS v  " +
            "INNER JOIN v.channel AS ch " +
            "INNER JOIN ch.profile WHERE v.visible=true ")
    Page<VideoEntity> findAll(Pageable pageable);
}
