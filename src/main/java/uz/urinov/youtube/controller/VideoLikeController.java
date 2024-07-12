package uz.urinov.youtube.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.urinov.youtube.enums.LikeStatus;
import uz.urinov.youtube.mapper.VideoLikeInfo;
import uz.urinov.youtube.service.VideoLikeService;

import java.util.List;

@RequestMapping("/video-like")
@RestController
@Slf4j
@Tag(name = "API s for video Like")
public class VideoLikeController {
    @Autowired
    private VideoLikeService videoLikeService;

    @PostMapping("/like&dislike/{videoId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "create video like")
    public ResponseEntity<String> likeOrDislike(@PathVariable String videoId,
                                                @RequestParam LikeStatus likeStatus) {
        log.info("likeOrDislike called for videoId: {}", videoId);
        return ResponseEntity.ok(videoLikeService.likeOrDislike(videoId,likeStatus));

    }

    @GetMapping("/user-liked-videos/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "All videos liked by a user")
    public ResponseEntity<Page<VideoLikeInfo>> getUserLikedVideos(@PathVariable Integer userId,
                                                                  @RequestParam(name = "page",defaultValue = "1") int page,
                                                                  @RequestParam(name = "size",defaultValue = "3") int size) {
        log.info("userLikedVideos called for userId: {}", userId);
        return ResponseEntity.ok(videoLikeService.getUserLikedVideos(userId,page-1,size));
    }
}
