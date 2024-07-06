package uz.urinov.youtube.service;

import org.springframework.stereotype.Service;
import uz.urinov.youtube.entity.VideoTagEntity;
import uz.urinov.youtube.repository.VideoTagRepository;

import java.util.List;

@Service
public class VideoTagService {
    private VideoTagRepository videoTagRepository;
    private TagService tagService;
    public VideoTagService(VideoTagRepository videoTagRepository,TagService tagService) {
        this.videoTagRepository = videoTagRepository;
        this.tagService = tagService;
    }

    public void createVideoTag(String videoId, List<Integer> tags) {
        for (Integer tagId : tags) {
            VideoTagEntity videoTagEntity = new VideoTagEntity();
            videoTagEntity.setVideoId(videoId);
            videoTagEntity.setTagId(tagId);
            videoTagRepository.save(videoTagEntity);
        }



    }
}
