package uz.urinov.youtube.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.urinov.youtube.dto.attach.AttachDTO;
import uz.urinov.youtube.dto.channel.ChannelResponseDTO;
import uz.urinov.youtube.dto.playlist.PlaylistResponseDTO;
import uz.urinov.youtube.dto.playlistVideo.PlaylistVideoCreateDTO;
import uz.urinov.youtube.dto.video.VideoDTO;
import uz.urinov.youtube.entity.PlaylistEntity;
import uz.urinov.youtube.entity.PlaylistVideoEntity;
import uz.urinov.youtube.mapper.PlayListInfoMapper;
import uz.urinov.youtube.mapper.PlaylistVideoInfoMapper;
import uz.urinov.youtube.repository.PlaylistRepository;
import uz.urinov.youtube.repository.PlaylistVideoRepository;
import uz.urinov.youtube.util.Result;

import java.util.List;
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

    // 4. Get Video list by playListId (video status published) PlaylistVideoInfo
    public List<PlaylistResponseDTO> getPlaylistVideoList(Integer playListId) {
        List<PlaylistVideoInfoMapper> mapperList = playlistVideoRepository.getVideoListByPlayListId(playListId);
        return mapperList.stream().map(this::toShortInfo).toList();
    }


    public PlaylistResponseDTO toShortInfo(PlaylistVideoInfoMapper playListInfoMapper) {
        PlaylistResponseDTO playlistResponseDTO = new PlaylistResponseDTO();
        playlistResponseDTO.setId(playListInfoMapper.getPlaylistId());
        playlistResponseDTO.setOrderNum(playListInfoMapper.getOrderNumber());
        playlistResponseDTO.setCreated(playListInfoMapper.getCreatedDate());

        ChannelResponseDTO channelResponseDTO = new ChannelResponseDTO();
        channelResponseDTO.setId(playListInfoMapper.getChannelId());
        channelResponseDTO.setName(playListInfoMapper.getChannelName());
        playlistResponseDTO.setChannel(channelResponseDTO);

        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setId(playListInfoMapper.getVideoId());
        videoDTO.setPreviewAttachId(playListInfoMapper.getPreviewAttachId());
        videoDTO.setTitle(playListInfoMapper.getVideoTitle());
        playlistResponseDTO.setVideoDTO(videoDTO);

        AttachDTO attachDTO=new AttachDTO();
        attachDTO.setDuration(playListInfoMapper.getDuration());
        playlistResponseDTO.setAttachDTO(attachDTO);

        return playlistResponseDTO;

    }
}
