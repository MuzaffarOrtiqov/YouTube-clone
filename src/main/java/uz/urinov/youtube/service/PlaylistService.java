package uz.urinov.youtube.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.urinov.youtube.dto.channel.ChannelResponseDTO;
import uz.urinov.youtube.dto.playlist.*;
import uz.urinov.youtube.dto.profile.ProfileResponseDTO;
import uz.urinov.youtube.dto.video.VideoShortDTO;
import uz.urinov.youtube.entity.PlaylistEntity;
import uz.urinov.youtube.entity.ProfileEntity;
import uz.urinov.youtube.enums.PlaylistStatus;
import uz.urinov.youtube.enums.ProfileRole;
import uz.urinov.youtube.exp.AppBadException;
import uz.urinov.youtube.mapper.PlayListInfoMapper;
import uz.urinov.youtube.mapper.PlaylistVideoInfoMapper;
import uz.urinov.youtube.repository.PlaylistRepository;
import uz.urinov.youtube.repository.PlaylistVideoRepository;
import uz.urinov.youtube.util.Result;
import uz.urinov.youtube.util.SecurityUtil;

import java.util.List;
import java.util.Optional;


@Service
public class PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private ChannelService channelService;
    @Autowired
    private PlaylistVideoRepository playlistVideoRepository;

    //    1. Create Playlist (USER)
    public PlaylistResponseDTO createPlaylist(PlaylistCreateDTO dto) {
        channelService.get(dto.getChannelId());
        Optional<PlaylistEntity> playlistEntityOptional = playlistRepository.findByNameAndChannelId(dto.getName(), dto.getChannelId());
        if (playlistEntityOptional.isPresent()) {
            throw new AppBadException("playlist already exists");
        }
        PlaylistEntity playlistEntity = new PlaylistEntity();
        playlistEntity.setName(dto.getName());
        playlistEntity.setDescription(dto.getDescription());
        playlistEntity.setStatus(dto.getStatus());
        playlistEntity.setOrderNum(dto.getOrderNum());
        playlistEntity.setChannelId(dto.getChannelId());
        playlistEntity.setProfileId(SecurityUtil.getProfileId());
        playlistEntity.setVideoCount(0);
        playlistEntity = playlistRepository.save(playlistEntity);
        return toDTO(playlistEntity);
    }

    // 2. Update Playlist(USER and OWNER)
    public PlaylistResponseDTO updatePlaylist(Integer playlistId, PlaylistUpdateDTO dto) {
        Integer profileId = SecurityUtil.getProfileId();
        Optional<PlaylistEntity> entityOptional = playlistRepository.findByIdAndProfileId(playlistId, profileId);
        if (entityOptional.isEmpty()) {
            throw new AppBadException("playlist does not exist");
        }
        PlaylistEntity playlistEntity = entityOptional.get();
        playlistEntity.setName(dto.getName());
        playlistEntity.setDescription(dto.getDescription());
        playlistEntity.setOrderNum(dto.getOrderNum());
        playlistRepository.save(playlistEntity);
        return toDTO(playlistEntity);
    }

    //  3. Change Playlist Status (USER and OWNER)
    public PlaylistResponseDTO statusPlaylist(Integer playlistId, PlaylistStatus status) {
        PlaylistEntity playlist = getPlaylist(playlistId);
        ProfileEntity profile = SecurityUtil.getProfile();
        if (profile.getRole().equals(ProfileRole.ROLE_ADMIN)) {
            playlist.setStatus(status);
            playlistRepository.save(playlist);
            return toDTO(playlist);
        }
        if (profile.getId().equals(playlist.getId())) {
            playlist.setStatus(status);
            playlistRepository.save(playlist);
            return toDTO(playlist);
        }
        throw new AppBadException("playlist does not exist");
    }

    // 4. Delete Playlist (USER and OWNER, ADMIN)
    public Result deletePlaylist(Integer playlistId) {
        PlaylistEntity playlist = getPlaylist(playlistId);
        ProfileEntity profile = SecurityUtil.getProfile();
        if (profile.getRole().equals(ProfileRole.ROLE_ADMIN)) {
            playlistRepository.delete(playlist);
            return new Result(playlist.getName() + " listi o'chirildi", true);
        }
        if (profile.getId().equals(playlist.getProfileId())) {
            playlistRepository.delete(playlist);
            return new Result(playlist.getName() + " listi o'chirildi", true);
        }
        throw new AppBadException("playlist does not exist");
    }

    // 5. Playlist Pagination (ADMIN) PlayListInfo
    public PageImpl<PlaylistResponseDTO> listPlaylistPage(int page, int size) {
        ProfileEntity profile = SecurityUtil.getProfile();
        if (!profile.getRole().equals(ProfileRole.ROLE_ADMIN)) {
            throw new AppBadException("profile does not have ROLE_ADMIN");
        }
        Pageable pageable = PageRequest.of(page, size);

        Page<PlayListInfoMapper> playListInfoMappers = playlistRepository.listPlaylistPage(pageable);

        List<PlaylistResponseDTO> playListResponseDTOs = playListInfoMappers.stream().map(this::toShortInfo).toList();

        return new PageImpl<>(playListResponseDTOs, pageable, playListInfoMappers.getTotalElements());
    }

    // 6. Playlist List By UserId (order by order number desc) (ADMIN) PlayListInfo
    public List<PlaylistResponseDTO> listPlaylistByUserId(Integer userId) {
        ProfileEntity profile = SecurityUtil.getProfile();
        if (!profile.getRole().equals(ProfileRole.ROLE_ADMIN)) {
            throw new AppBadException("profile does not have ROLE_ADMIN");
        }
        List<PlayListInfoMapper> playlistEntityList = playlistRepository.findAllByProfileIdOrderByOrderNumDesc(userId);
        return playlistEntityList.stream().map(this::toShortInfo).toList();
    }

    // 7. Get User Playlist (order by order number desc) (murojat qilgan user ni) PlayListShortInfo
    public List<PlayListShortInfoDTO> listPlaylistByUser() {
        ProfileEntity profile = SecurityUtil.getProfile();
        List<PlaylistEntity> playlistEntityList = playlistRepository.findAllByProfileId(profile.getId());
        return playlistEntityList.stream().map(this::toShortDTO).toList();
    }

    // 8. Get Channel Play List By ChannelKey (order by order_num desc) (only Public) PlayListShortInfo
    public List<PlayListShortInfoDTO> listPlaylistByUserAll(String channelId) {

        List<PlaylistEntity> playlistEntityList = playlistRepository.findAllByChannelIdAndStatus(channelId, PlaylistStatus.PUBLIC);
        return playlistEntityList.stream().map(this::toShortDTO).toList();
    }

    // 9.Get Playlist by id
    // id,name,video_count, total_view_count (shu play listdagi videolarni ko'rilganlar soni), last_update_date
    public PlaylistResponseDTO getPlaylistById(Integer id) {
        PlaylistEntity playlist = getPlaylist(id);
        Long count = playlistVideoRepository.viewCountByPlaylistId(id);
        PlaylistResponseDTO playlistResponseDTO = new PlaylistResponseDTO();
        playlistResponseDTO.setId(id);
        playlistResponseDTO.setName(playlist.getName());
        playlistResponseDTO.setVideoCount(playlist.getVideoCount());
        playlistResponseDTO.setPlaylistTotalViewCount(count);
        return playlistResponseDTO;
    }

    public PlaylistResponseDTO toDTO(PlaylistEntity entity) {
        PlaylistResponseDTO playlistResponseDTO = new PlaylistResponseDTO();
        playlistResponseDTO.setId(entity.getId());
        playlistResponseDTO.setName(entity.getName());
        playlistResponseDTO.setDescription(entity.getDescription());
        playlistResponseDTO.setStatus(entity.getStatus());
        playlistResponseDTO.setOrderNum(entity.getOrderNum());
        playlistResponseDTO.setVideoCount(entity.getVideoCount());

        ChannelResponseDTO channelResponseDTO = new ChannelResponseDTO();
        channelResponseDTO.setId(entity.getChannelId());
        channelResponseDTO.setName(entity.getChannel().getName());
        channelResponseDTO.setPhoto(entity.getChannel().getPhotoId());

        playlistResponseDTO.setChannel(channelResponseDTO);

        ProfileResponseDTO profileResponseDTO = new ProfileResponseDTO();
        profileResponseDTO.setId(entity.getProfileId());
        profileResponseDTO.setName(entity.getProfile().getName());
        profileResponseDTO.setSurname(entity.getProfile().getSurname());
        profileResponseDTO.setPhotoId(entity.getProfile().getPhotoId());

        playlistResponseDTO.setProfile(profileResponseDTO);

        playlistResponseDTO.setCreated(entity.getCreated());
        return playlistResponseDTO;

    }

    public PlayListShortInfoDTO toShortDTO(PlaylistEntity entity) {

        PlayListShortInfoDTO playlistShortDTO = new PlayListShortInfoDTO();
        playlistShortDTO.setId(entity.getId());
        playlistShortDTO.setName(entity.getName());
        playlistShortDTO.setVideoCount(entity.getVideoCount());
        playlistShortDTO.setCreated(entity.getCreated());

        ChannelShortInfoDTO channelResponseDTO = new ChannelShortInfoDTO();
        channelResponseDTO.setId(entity.getChannelId());
        channelResponseDTO.setName(entity.getChannel().getName());

        playlistShortDTO.setChannel(channelResponseDTO);

        List<PlaylistVideoInfoMapper> listByPlayListId = playlistVideoRepository.getVideoListByPlayListId(entity.getId());
        List<VideoShortDTO> videoShortDTOList = listByPlayListId.stream().map(this::toVideoShortDTO).toList();

        playlistShortDTO.setVideoList(videoShortDTOList);


        return playlistShortDTO;
    }

    public PlaylistResponseDTO toShortInfo(PlayListInfoMapper playListInfoMapper) {
        PlaylistResponseDTO playlistResponseDTO = new PlaylistResponseDTO();
        playlistResponseDTO.setId(playListInfoMapper.getPlaylistId());
        playlistResponseDTO.setName(playListInfoMapper.getPlaylistName());

        ChannelResponseDTO channelResponseDTO = new ChannelResponseDTO();
        channelResponseDTO.setId(playListInfoMapper.getChannelId());
        channelResponseDTO.setName(playListInfoMapper.getChannelName());
        playlistResponseDTO.setChannel(channelResponseDTO);

        playlistResponseDTO.setCreated(playListInfoMapper.getPlaylistCreated());

        return playlistResponseDTO;

    }

    public VideoShortDTO toVideoShortDTO(PlaylistVideoInfoMapper playListVideoMapper) {
        VideoShortDTO videoShortDTO = new VideoShortDTO();

        videoShortDTO.setId(playListVideoMapper.getVideoId());
        videoShortDTO.setVideoTitle(playListVideoMapper.getVideoTitle());
        videoShortDTO.setDuration(playListVideoMapper.getDuration());

        return videoShortDTO;

    }

    public PlaylistEntity getPlaylist(Integer playlistId) {
        return playlistRepository.findById(playlistId).orElseThrow(() ->
                new AppBadException("playlist does not exist"));
    }


    public PlaylistResponseDTO getPlaylistResponseDTO(Integer playlistId) {
        PlaylistEntity playlistEntity = getPlaylist(playlistId);
        PlaylistResponseDTO playlistResponseDTO = new PlaylistResponseDTO();
        playlistResponseDTO.setId(playlistEntity.getId());
        playlistResponseDTO.setName(playlistEntity.getName());
        return playlistResponseDTO;
    }

    public List<PlaylistResponseDTO> getByChannelId(String channelId) {
        List<PlaylistEntity> playlistEntityList = playlistRepository.findAllByChannelIdAndStatus(channelId, PlaylistStatus.PUBLIC);
        List<PlaylistResponseDTO> result = playlistEntityList
                .stream()
                .map(playlistEntity -> {
                    PlaylistResponseDTO playlistResponseDTO = new PlaylistResponseDTO();
                    playlistResponseDTO.setId(playlistEntity.getId());
                    playlistResponseDTO.setName(playlistEntity.getName());
                    return playlistResponseDTO;
                }).toList();
        return result;
    }
}
