package uz.urinov.youtube.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.urinov.youtube.dto.tag.TagDTO;
import uz.urinov.youtube.dto.videotag.VideoTagDTO;
import uz.urinov.youtube.dto.videotag.VideoTagShortDto;
import uz.urinov.youtube.service.VideoTagService;

import java.util.List;

@RequestMapping("/video-tag")
@RestController
@Tag(name = "All API s for video tag")
@Slf4j
public class VideoTagController {
    private VideoTagService videoTagService;
    public VideoTagController(VideoTagService videoTagService) {
        this.videoTagService = videoTagService;
    }
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Create tag for a video ")
    public ResponseEntity<VideoTagDTO> createVideoTag(@Valid @RequestBody VideoTagDTO videoTagDTO) {
        log.info("Creating tag with id : {}", videoTagDTO.getTagId());
        VideoTagDTO response = videoTagService.createTag(videoTagDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Delete a tag")
    public ResponseEntity<String> deleteTag(@RequestParam(name = "tagId") Integer tagId,
                                            @RequestParam(name = "videoId") String videoId) {
        log.info("Deleting tag with id : {}", tagId);
        return ResponseEntity.ok(videoTagService.deleteTag(tagId,videoId));
    }

    @GetMapping("/tag-list")
    @Operation(summary ="Get all tags in a video")
    public ResponseEntity<List<VideoTagDTO>> getAllTags(@RequestParam String videoId) {
        log.info("Get all tags in a video with id : {}", videoId);
        return ResponseEntity.ok(videoTagService.getAllTags(videoId));
    }
}
