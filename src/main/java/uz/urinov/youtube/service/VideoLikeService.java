package uz.urinov.youtube.service;

import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.urinov.youtube.dto.attach.AttachDTO;
import uz.urinov.youtube.entity.VideoEntity;
import uz.urinov.youtube.entity.VideoLikeEntity;
import uz.urinov.youtube.enums.LikeStatus;
import uz.urinov.youtube.mapper.VideoLikeInfo;
import uz.urinov.youtube.repository.VideoLikeRepository;
import uz.urinov.youtube.util.MapperUtil;
import uz.urinov.youtube.util.SecurityUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class VideoLikeService {
    @Autowired
    private VideoLikeRepository videoLikeRepository;
    @Autowired
    private AttachService attachService;
    @Autowired
    private VideoService videoService;

    public String likeOrDislike(String videoId, LikeStatus likeStatus) {

        Integer profileId = SecurityUtil.getProfileId();
        Optional<VideoLikeEntity> optional = videoLikeRepository.hasLikedTheVideo(profileId, videoId);
        if (optional.isEmpty()) {
            VideoLikeEntity videoLikeEntity = new VideoLikeEntity();
            videoLikeEntity.setVideoId(videoId);
            videoLikeEntity.setProfileId(profileId);
            videoLikeEntity.setLikeStatus(likeStatus);
            videoLikeRepository.save(videoLikeEntity);
        }
        if (optional.isPresent()) {
            VideoLikeEntity videoLikeEntity = optional.get();
            if (videoLikeEntity.getLikeStatus().equals(likeStatus)) {
                videoLikeRepository.delete(videoLikeEntity);
                return "Reaction taken from video " + videoId;
            }
            if (!(videoLikeEntity.getLikeStatus().equals(likeStatus))) {
                videoLikeEntity.setLikeStatus(likeStatus);
                videoLikeRepository.save(videoLikeEntity);
            }
        }
        return videoId + " " + likeStatus.name();
    }

    public List<VideoLikeInfo> getUserLikedVideos(Integer userId) {
        List<Object[]> userLikedVideos = videoLikeRepository.getUserLikedVideos(userId);
        return userLikedVideos.stream().map(objects -> {
            VideoLikeInfo videoLikeInfo = new VideoLikeInfo();
            videoLikeInfo.setId(MapperUtil.getLongValue(objects[0]));
            videoLikeInfo.setVideoShortInfo(videoService.getVideoShortInfoById(MapperUtil.getStringValue(objects[1]), MapperUtil.getStringValue(objects[2])));
            videoLikeInfo.setAttachShortInfo(attachService.getAttachShortInfoByPreviewAttachId(MapperUtil.getStringValue(objects[3])));
            return videoLikeInfo;
        }).toList();
    }
}
