package uz.urinov.youtube.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.urinov.youtube.entity.PlaylistVideoEntity;
import uz.urinov.youtube.mapper.PlaylistVideoInfoMapper;

import java.util.List;
import java.util.Optional;

public interface PlaylistVideoRepository extends JpaRepository<PlaylistVideoEntity,Integer> {

    // 2. Update (playlist_id,video_id, order_num) front dan keladigan malumotlar
    Optional<PlaylistVideoEntity> findByPlaylistIdAndVideoId(Integer playlistId, String videoId);



    @Query("select sum(p.video.viewCount) from PlaylistVideoEntity  p where p.playlistId=?1")
    Long viewCountByPlaylistId(Integer playlistId);

    @Query(value = "SELECT pv.playlist_id, pv.video_id,pv.order_num, pv.create_date,\n" +
            "v.preview_attach_id,v.title, a.duration,\n" +
            "ch.id As channel_id, ch.name AS channel_name\n" +
            "FROM playlist_video AS pv\n" +
            "INNER JOIN video AS v ON pv.video_id=v.id\n" +
            "INNER JOIN channel AS ch ON v.channel_id=ch.id\n" +
            "INNER JOIN attach AS a ON v.attach_id=a.id;",nativeQuery = true)
    List<PlaylistVideoInfoMapper> getVideoListByPlayListId();



}
