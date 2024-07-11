package uz.urinov.youtube.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.urinov.youtube.entity.PlaylistVideoEntity;
import uz.urinov.youtube.mapper.PlaylistVideoInfoMapper;

import java.util.List;
import java.util.Optional;

public interface PlaylistVideoRepository extends JpaRepository<PlaylistVideoEntity, Integer> {

    // 2. Update (playlist_id,video_id, order_num) front dan keladigan malumotlar
    Optional<PlaylistVideoEntity> findByPlaylistIdAndVideoId(Integer playlistId, String videoId);


    @Query("select sum(p.video.viewCount) from PlaylistVideoEntity  p where p.playlistId=?1")
    Long viewCountByPlaylistId(Integer playlistId);


    // 4. Get Video list by playListId (video status published) PlaylistVideoInfo
    @Query(value = "SELECT pv.playlist_id AS playlistId, pv.video_id AS videoId," +
            " v.preview_attach_id AS previewAttachId, v.title AS videoTitle, a.duration," +
            " ch.id As channelId, ch.name AS channelName, pv.order_num AS oderNumber, pv.create_date AS createdDate" +
            " FROM playlist_video AS pv" +
            " INNER JOIN video AS v ON pv.video_id=v.id" +
            " INNER JOIN channel AS ch ON v.channel_id=ch.id" +
            " INNER JOIN attach AS a ON v.attach_id=a.id where pv.playlist_id=?1;", nativeQuery = true)
    List<PlaylistVideoInfoMapper> getVideoListByPlayListId(Integer id);


}
