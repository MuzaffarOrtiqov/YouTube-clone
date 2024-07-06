package uz.urinov.youtube.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.urinov.youtube.dto.video.VideoCreateDTO;
import uz.urinov.youtube.dto.video.VideoDTO;
import uz.urinov.youtube.dto.video.VideoUpdateDTO;
import uz.urinov.youtube.enums.VideoStatus;
import uz.urinov.youtube.service.VideoService;

import java.util.List;

@RestController
@RequestMapping("/video")
@Slf4j
@Tag(name = "API s for a video", description = "List of API s for a video ")
public class VideoController {
    private VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Create a video")
    public ResponseEntity<VideoDTO> createVideo(@Valid @RequestBody VideoCreateDTO videoCreateDTO) {
        log.info("Creating a video with name : {}", videoCreateDTO.getAttachId());
        return ResponseEntity.ok(videoService.create(videoCreateDTO));
    }

    @PutMapping("/update-video-details/{videoId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Update video details")
    public ResponseEntity<VideoDTO> updateVideo(@PathVariable String videoId, @RequestBody VideoUpdateDTO videoUpdateDTO) {
        log.info("updating video with id  : {}",videoId);
        return ResponseEntity.ok(videoService.update(videoId,videoUpdateDTO));
    }

    @PutMapping("/update-status/{attachId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "change status", description = "Status of teh video can be change into a public video")
    public ResponseEntity<Boolean> updateVideoStatus(@PathVariable String attachId,
                                                     @RequestParam(name = "status") VideoStatus status) {
        log.info("Video status changing : {} ", attachId);
        return ResponseEntity.ok(videoService.updateStatus(attachId, status));
    }

    @PutMapping("/increase-view-count")
    @Operation(summary = "Increase view count ")
    public ResponseEntity<String> increaseViewCount(@RequestParam(name = "attachId") String attachId) {
        log.info("Increase view count : {}", attachId);
        return ResponseEntity.ok(videoService.increaseViewCount(attachId));
    }

    @GetMapping("/pagination/{categoryId}")
    @Operation(summary = "Get All Videos in one category")
    public ResponseEntity<Page<VideoDTO>> getAllVideos(@PathVariable(name = "categoryId") Integer categoryId,
                                                       @RequestParam(name = "page", defaultValue = "1") int page,
                                                       @RequestParam(name = "size", defaultValue = "3") int size) {
        log.info("Gettinng all videos with category id : {}", categoryId);
        return ResponseEntity.ok(videoService.pagination(categoryId, page - 1, size));
    }

    @GetMapping("/search-by-title")
    @Operation(summary = "search video by its title")
    public ResponseEntity<List<VideoDTO>> searchVideoByTitle(@RequestParam(name = "title") String title) {
        log.info("search video by its title : {}", title);
        return ResponseEntity.ok(videoService.searchVideoByTitle(title));
    }

    @GetMapping("/get-by-tag-id/{tagId}")
    @Operation(summary = "Get video by tag id")
    public ResponseEntity<Page<VideoDTO>> paginationWithTag(@PathVariable Integer tagId,
                                                            @RequestParam(name = "page", defaultValue = "1") int page,
                                                            @RequestParam(name = "size", defaultValue = "3") int size) {
        log.info("pagination with tag id : {}", tagId);
        return ResponseEntity.ok(videoService.paginationWithTag(tagId,page-1,size));
    }
    @GetMapping("/get-video-by-id/{videoId}")
    @Operation(summary = "Get everthing about the video")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<VideoDTO> getVideoById(@PathVariable String videoId) {
        log.info("get video by id : {}", videoId);
        return ResponseEntity.ok(videoService.getVideoFullInfoById(videoId));
    }


}
