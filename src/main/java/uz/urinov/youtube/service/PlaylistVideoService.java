package uz.urinov.youtube.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.urinov.youtube.dto.playlistVideo.PlaylistVideoCreateDTO;
import uz.urinov.youtube.entity.PlaylistEntity;
import uz.urinov.youtube.entity.PlaylistVideoEntity;
import uz.urinov.youtube.repository.PlaylistRepository;
import uz.urinov.youtube.repository.PlaylistVideoRepository;
import uz.urinov.youtube.util.Result;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaylistVideoService {
    private final PlaylistVideoRepository playlistVideoRepository;
    private final PlaylistService playlistService;
    private final VideoService videoService;
    private final PlaylistRepository playlistRepository;



    //  1. Create (User and Owner) (playlist_id,video_id, order_num) front dan keladigan malumotlar
    public Result create(PlaylistVideoCreateDTO playlistVideoCreateDTO) {
        PlaylistEntity playlist = playlistService.getPlaylist(playlistVideoCreateDTO.getPlaylistId());
        videoService.getVideoById(playlistVideoCreateDTO.getVideoId());
        PlaylistVideoEntity playlistVideoEntity = new PlaylistVideoEntity();
        playlistVideoEntity.setPlaylistId(playlistVideoCreateDTO.getPlaylistId());
        playlistVideoEntity.setVideoId(playlistVideoCreateDTO.getVideoId());
        playlistVideoEntity.setOrderNum(playlistVideoCreateDTO.getOrderNum());
        playlistVideoRepository.save(playlistVideoEntity);
        playlist.setVideoCount(playlist.getVideoCount()+1);
        playlistRepository.save(playlist);
        return new Result("PlaylistVideo create",true);
    }

    // 2. Update (playlist_id,video_id, order_num) front dan keladigan malumotlar
    public Result update(PlaylistVideoCreateDTO playlistVideoCreateDTO) {
        Optional<PlaylistVideoEntity> playlistVideoEntityOptional = playlistVideoRepository.findByPlaylistIdAndVideoId(playlistVideoCreateDTO.getPlaylistId(), playlistVideoCreateDTO.getVideoId());
        if (playlistVideoEntityOptional.isEmpty()) {
            throw new RuntimeException("PlaylistVideo not found");
        }
        PlaylistVideoEntity playlistVideoEntity = playlistVideoEntityOptional.get();
        playlistVideoEntity.setPlaylistId(playlistVideoCreateDTO.getPlaylistId());
        playlistVideoEntity.setVideoId(playlistVideoCreateDTO.getVideoId());
        playlistVideoEntity.setOrderNum(playlistVideoCreateDTO.getOrderNum());
        playlistVideoRepository.save(playlistVideoEntity);
        return new Result("PlaylistVideo update",true);
    }

    //  3. Delete PlayListVideo (playlist_id,video_id) front dan keladigan malumotlar
    public Result delete(Integer playlistId, String videoId) {
        Optional<PlaylistVideoEntity> playlistVideoEntityOptional = playlistVideoRepository.findByPlaylistIdAndVideoId(playlistId, videoId);
        if (playlistVideoEntityOptional.isEmpty()) {
            throw new RuntimeException("PlaylistVideo not found");
        }
        PlaylistVideoEntity playlistVideoEntity = playlistVideoEntityOptional.get();
        playlistVideoRepository.delete(playlistVideoEntity);
        return new Result("PlaylistVideo delete",true);
    }
}
