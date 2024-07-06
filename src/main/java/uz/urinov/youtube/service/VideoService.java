package uz.urinov.youtube.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.urinov.youtube.dto.video.VideoCreateDTO;
import uz.urinov.youtube.dto.video.VideoDTO;
import uz.urinov.youtube.dto.video.VideoUpdateDTO;
import uz.urinov.youtube.entity.ProfileEntity;
import uz.urinov.youtube.entity.VideoEntity;
import uz.urinov.youtube.entity.VideoTagEntity;
import uz.urinov.youtube.enums.ProfileRole;
import uz.urinov.youtube.enums.VideoStatus;
import uz.urinov.youtube.exp.AppBadException;
import uz.urinov.youtube.repository.VideoRepository;
import uz.urinov.youtube.repository.VideoTagRepository;
import uz.urinov.youtube.util.SecurityUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class VideoService {
    private VideoRepository videoRepository;
    private AttachService attachService;
    private VideoTagService videoTagService;
    private CategoryService categoryService;
    private TagService tagService;
    private VideoTagRepository videoTagRepository;
    private ChannelService channelService;

    public VideoService(VideoRepository videoRepository,
                        AttachService attachService, VideoTagService videoTagService,
                        CategoryService categoryService, TagService tagService,
                        VideoTagRepository videoTagRepository, ChannelService channelService) {
        this.videoRepository = videoRepository;
        this.attachService = attachService;
        this.videoTagService = videoTagService;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.videoTagRepository = videoTagRepository;
        this.channelService = channelService;
    }

    //    1. Create Video (USER)
    public VideoDTO create(VideoCreateDTO videoCreateDTO) {
        VideoEntity videoEntity = new VideoEntity();
        videoEntity.setPreviewAttachId(videoEntity.getPreviewAttachId());
        videoEntity.setTitle(videoCreateDTO.getTitle());
        videoEntity.setCategoryId(videoCreateDTO.getCategoryId());
        videoEntity.setAttachId(videoCreateDTO.getAttachId());
        videoEntity.setStatus(VideoStatus.PRIVATE);
        videoEntity.setType(videoCreateDTO.getType());
        videoEntity.setChannelId(videoCreateDTO.getChannelId());
        videoRepository.save(videoEntity);
        videoTagService.createVideoTag(videoEntity.getId(), videoCreateDTO.getTags());
        return toDTO(videoEntity);
    }

    //   2. Update Video Detail (USER and OWNER)
    public VideoDTO update(String videoId, VideoUpdateDTO videoUpdateDTO) {
        if (isOwner(videoId)) {
            VideoEntity videoEntity = getVideoById(videoId);
            videoEntity.setTitle(videoUpdateDTO.getTitle() == null ? videoEntity.getTitle() : videoUpdateDTO.getTitle());
            videoEntity.setDescription(videoUpdateDTO.getDescription() == null ? videoEntity.getDescription() : videoUpdateDTO.getDescription());
            videoRepository.save(videoEntity);
            return toDTO(videoEntity);
        }
        throw new AppBadException("Not authorized to update this video");
    }

    // 3. Change Video Status (USER and OWNER)
    public Boolean updateStatus(String attachId, VideoStatus status) {
        videoRepository.updateStatus(attachId, status);
        return true;
    }

    //    4. Increase video_view Count by id
    public String increaseViewCount(String attachId) {
        videoRepository.increaseViewCount(attachId);
        return "Increased by one";
    }

    // 5. Get Video Pagination by CategoryId
    public Page<VideoDTO> pagination(Integer categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VideoEntity> pageObj = videoRepository.pagination(categoryId, pageable);
        List<VideoDTO> videoDTOList = new LinkedList<>();
        Long total = pageObj.getTotalElements();
        pageObj.forEach(videoEntity -> {
            videoDTOList.add(toShortInfo(videoEntity));
        });
        return new PageImpl<>(videoDTOList, pageable, total);
    }

    //  6. Search video by Title
    public List<VideoDTO> searchVideoByTitle(String title) {
        List<VideoEntity> entityList = videoRepository.findByTile(title);
        List<VideoDTO> videoDTOList = new LinkedList<>();
        entityList.forEach(videoEntity -> {
            videoDTOList.add(toShortInfo(videoEntity));
        });
        return videoDTOList;
    }

    //  7. Get video by tag_id with pagination
    public Page<VideoDTO> paginationWithTag(Integer tagId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<VideoEntity> pageObj = videoRepository.paginationWithTagId(tagId, pageable);
        List<VideoDTO> videoDTOList = new LinkedList<>();
        Long total = pageObj.getTotalElements();
        pageObj.forEach(videoEntity -> {
            videoDTOList.add(toShortInfo(videoEntity));
        });
        return new PageImpl<>(videoDTOList, pageable, total);
    }

    // 8. Get Video By id (If Status PRIVATE allow only for OWNER or ADMIN)
    public VideoDTO getVideoFullInfoById(String videoId) {
        VideoEntity videoEntity = getVideoById(videoId);
        if (videoEntity.getStatus().equals(VideoStatus.PRIVATE)) {
            if (isOwnerOrAdmin(videoId)) {
                return toFullInfo(videoEntity);
            }
            throw new AppBadException("No access to get the video");
        }
        return toFullInfo(videoEntity);
    }

    public boolean isOwnerOrAdmin(String videoId) {
        VideoEntity videoEntity = getVideoById(videoId);
        ProfileEntity profile = SecurityUtil.getProfile();
        if (profile.getRole().equals(ProfileRole.ROLE_ADMIN) || isOwner(videoId)) {
            return true;
        }
        return false;
    }

    public VideoEntity getVideoById(String videoId) {
        Optional<VideoEntity> optional = videoRepository.findById(videoId);
        if (optional.isEmpty()) {
            throw new AppBadException("Video not found");
        }
        return optional.get();
    }

    public VideoDTO toFullInfo(VideoEntity videoEntity) {
        VideoDTO videoDTO = new VideoDTO();

        videoDTO.setId(videoEntity.getId());
        videoDTO.setTitle(videoEntity.getTitle());
        videoDTO.setDescription(videoEntity.getDescription());
        videoDTO.setPreviewAttach(attachService.getDTOWithURL(videoEntity.getPreviewAttachId()));
        videoDTO.setAttach(attachService.getDTOWithURL(videoEntity.getAttachId()));
        videoDTO.setCategory(categoryService.getCategoryDTOById(videoEntity.getCategoryId()));
        videoDTO.setTag(tagService.getTagDTOById(videoTagRepository.findTagIdByVideoId(videoEntity.getId())));
        videoDTO.setPublishedDate(videoEntity.getPublishedDate());
        videoDTO.setChannel(channelService.getChannelDTOByChannelId(videoEntity.getChannelId()));
        videoDTO.setViewCount(videoEntity.getViewCount());
        videoDTO.setSharedCount(videoEntity.getSharedCount());
        /*  isUserLiked,IsUserDisliked),duration*/
        //TODO videoFullInfo SET Like
        return videoDTO;
    }

    public VideoDTO toDTO(VideoEntity videoEntity) {
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setId(videoEntity.getId());
        videoDTO.setTitle(videoEntity.getTitle());
        videoDTO.setCategoryId(videoEntity.getCategoryId());
        videoDTO.setAttachId(videoEntity.getAttachId());
        videoDTO.setStatus(videoEntity.getStatus());
        videoDTO.setType(videoEntity.getType());
        videoDTO.setChannelId(videoEntity.getChannelId());
        return videoDTO;
    }

    public VideoDTO toShortInfo(VideoEntity videoEntity) {
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setId(videoEntity.getId());
        videoDTO.setTitle(videoEntity.getTitle());
        videoDTO.setAttach(attachService.getDTOWithURL(videoEntity.getAttachId()));
        videoDTO.setPublishedDate(videoEntity.getPublishedDate());
        videoDTO.setChannel(videoDTO.getChannel());
        videoDTO.setViewCount(videoEntity.getViewCount());
        return videoDTO;
    }

    public boolean isOwner(String videoId) {
        Integer userId = SecurityUtil.getProfileId();
        Integer profileId = videoRepository.findUser(videoId);
        if (!profileId.equals(userId) || profileId == null) {
            throw new AppBadException("No access to update video");
        }
        return true;
    }

}
