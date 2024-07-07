package uz.urinov.youtube.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uz.urinov.youtube.entity.PlaylistEntity;
import uz.urinov.youtube.enums.PlaylistStatus;
import uz.urinov.youtube.mapper.PlayListInfoMapper;


import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends CrudRepository<PlaylistEntity,Integer> {

    //    1. Create Playlist (USER)
    Optional<PlaylistEntity> findByNameAndChannelId(String name, String channelId);

    // 2. Update Playlist(USER and OWNER)
    Optional<PlaylistEntity> findByIdAndProfileId(Integer id, Integer profileId);

    // 5. Playlist Pagination (ADMIN) PlayListInfo
    @Query(value = "SELECT p.id AS playlistId, p.name AS playlistName, p.created AS playlistCreated, ch.id AS channelId, ch.name AS channelName " +
            "FROM PlaylistEntity as p inner join ChannelEntity as ch on p.channelId=ch.id order by p.orderNum desc ")
    Page<PlayListInfoMapper> listPlaylistPage(Pageable pageable);

    // 6. Playlist List By UserId (order by order number desc) (ADMIN) PlayListInfo
    @Query(value = "SELECT " +
            "p.id AS playlistId, p.name AS playlistName, p.created AS playlistCreated, " +
            "ch.id AS channelId, ch.name AS channelName " +
            "FROM PlaylistEntity as p " +
            "inner join ChannelEntity as ch on p.channelId=ch.id " +
            "where p.profileId=?1 " +
            "order by p.orderNum desc ")
    List<PlayListInfoMapper> findAllByProfileIdOrderByOrderNumDesc(Integer profileId);

    // 7. Get User Playlist (order by order number desc) (murojat qilgan user ni) PlayListShortInfo
    List<PlaylistEntity> findAllByProfileId(Integer profileId);

    // 8. Get Channel Play List By ChannelKey (order by order_num desc) (only Public) PlayListShortInfo
    List<PlaylistEntity> findAllByChannelIdAndStatus(String channelId, PlaylistStatus status);



}
