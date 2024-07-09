package uz.urinov.youtube.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.urinov.youtube.dto.tag.TagDTO;
import uz.urinov.youtube.dto.videotag.VideoTagDTO;
import uz.urinov.youtube.dto.videotag.VideoTagShortDto;
import uz.urinov.youtube.entity.VideoTagEntity;
import uz.urinov.youtube.exp.AppBadException;
import uz.urinov.youtube.repository.VideoRepository;
import uz.urinov.youtube.repository.VideoTagRepository;
import uz.urinov.youtube.util.SecurityUtil;

import java.util.List;

@Service
@Slf4j
public class VideoTagService {
    private VideoTagRepository videoTagRepository;


    public VideoTagService(VideoTagRepository videoTagRepository, TagService tagService, VideoService videoService) {
        this.videoTagRepository = videoTagRepository;

    }

    public void createVideoTag(String videoId, List<Integer> tags) {
        for (Integer tagId : tags) {
            VideoTagEntity videoTagEntity = new VideoTagEntity();
            videoTagEntity.setVideoId(videoId);
            videoTagEntity.setTagId(tagId);
            videoTagRepository.save(videoTagEntity);
        }

    }

    public List<VideoTagShortDto> findTagDtoListByVideoId(String videoId) {
        List<VideoTagShortDto> allByVideoId = videoTagRepository.findAllByVideoId(videoId);
        if (allByVideoId.isEmpty()) {
            throw new AppBadException("Tag not found");
        }
        return allByVideoId;
    }

    public VideoTagDTO createTag(VideoTagDTO videoTagDTO) {
        if (isOwner(videoTagDTO.getVideoId())) {
            VideoTagEntity videoTagEntity = new VideoTagEntity();
            videoTagEntity.setVideoId(videoTagDTO.getVideoId());
            videoTagEntity.setTagId(videoTagEntity.getTagId());
            videoTagRepository.save(videoTagEntity);
            return toDTO(videoTagEntity);
        }
        log.info("Not owner is trying to create tag for video id {}", videoTagDTO.getVideoId());
        throw new AppBadException("Not allowed to create tag");
    }
    public VideoTagDTO toDTO(VideoTagEntity videoTagEntity) {
        VideoTagDTO videoTagDTO = new VideoTagDTO();
        videoTagDTO.setId(videoTagEntity.getId());
        videoTagDTO.setVideoId(videoTagEntity.getVideoId());
        videoTagDTO.setTagId(videoTagEntity.getTagId());
        videoTagDTO.setCreatedDate(videoTagEntity.getCreatedDate());
        return videoTagDTO;
    }
    public boolean isOwner(String videoId) {
        Integer userId = SecurityUtil.getProfileId();
        Integer profileId = videoTagRepository.findUser(videoId);
        if (!profileId.equals(userId) || profileId == null) {
            throw new AppBadException("No access to update video");
        }
        return true;
    }


    public String deleteTag(Integer tagId, String videoId) {
        if (isOwner(videoId)) {
            List<Integer> videoTagList = videoTagRepository.findTagIdListByVideoId(videoId);
            if (videoTagList.isEmpty()) {
                throw new AppBadException("Tag not found");
            }
            if (videoTagList.contains(tagId)) {
                videoTagRepository.delete(videoId, tagId);
                return "Tag successfully deleted";
            }
        }
        throw new AppBadException("Not allowed to delete tag");
    }

    public List<VideoTagDTO> getAllTags(String videoId) {
        List<VideoTagEntity> videoTagList = videoTagRepository.findVideoTagEntitiesBy(videoId);
        List<VideoTagDTO> result = videoTagList
                .stream()
                .map(videoTagEntity -> {
                    VideoTagDTO videoTagDTO = new VideoTagDTO();
                    videoTagDTO.setVideoId(videoTagEntity.getVideoId());
                    videoTagDTO.setTagId(videoTagEntity.getTagId());
                    videoTagDTO.setCreatedDate(videoTagEntity.getCreatedDate());
                    return videoTagDTO;
                })
                .toList();
        return result;

    }
}
