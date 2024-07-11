package uz.urinov.youtube.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.urinov.youtube.dto.commentLike.CommentLikeCreateDto;
import uz.urinov.youtube.dto.commentLike.CommentLikeResponseDto;
import uz.urinov.youtube.service.CommentLikeService;

import java.util.List;


@RestController
@RequestMapping("/comment-like")
@RequiredArgsConstructor
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    // 1. Create Comment like (1 va 2 )
    @PostMapping("/like-dislike")
    public ResponseEntity<CommentLikeResponseDto> addCommentLikeAndDislike(@RequestBody CommentLikeCreateDto commentLikeCreateDto) {
        CommentLikeResponseDto articleLikeResponseDto=commentLikeService.addCommentLikeAndDislike(commentLikeCreateDto);
        return ResponseEntity.ok(articleLikeResponseDto);
    }

    // 3. User Liked Comment List (order by created_date desc) (USER)
    @GetMapping("/user-liked-comment-list")
    public ResponseEntity<List<CommentLikeResponseDto>> getUserLikedCommentList() {
        List<CommentLikeResponseDto> responseDtoList=commentLikeService.getUserLikedCommentList();
        return ResponseEntity.ok(responseDtoList);
    }
    // 4. Get User LikedComment List By UserId (ADMIN)
    @GetMapping("/user-liked-comment-list-userId/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<CommentLikeResponseDto>> getUserLikedCommentListUserId(@PathVariable("userId") int userId) {
        List<CommentLikeResponseDto> responseDtoList=commentLikeService.getUserLikedCommentListUserId(userId);
        return ResponseEntity.ok(responseDtoList);
    }


}