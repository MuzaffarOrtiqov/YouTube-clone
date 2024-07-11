package uz.urinov.youtube.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.urinov.youtube.dto.comment.CommentCreateDto;
import uz.urinov.youtube.dto.comment.CommentResponseDto;
import uz.urinov.youtube.service.CommentService;
import uz.urinov.youtube.util.Result;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 1. Crate Comment (USER)
    @PostMapping("/create")
    public ResponseEntity<Result> addComment(@Valid @RequestBody CommentCreateDto commentCreateDto) {
        Result result = commentService.addComment(commentCreateDto);
        return ResponseEntity.status(result.isSuccess() ? 201 : 409).body(result);
    }

    // 2. Update Comment (USER AND OWNER)
    @PutMapping("/update")
    public ResponseEntity<Result> updateComment(@Valid @RequestBody CommentCreateDto commentCreateDto) {
        Result result = commentService.updateComment(commentCreateDto);
        return ResponseEntity.status(result.isSuccess() ? 201 : 409).body(result);
    }

    // 3. Delete Comment (USER AND OWNER, ADMIN)
    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Result> deleteComment(@PathVariable Integer commentId) {
        Result result=commentService.delete(commentId);
        return ResponseEntity.status(result.isSuccess() ? 201 : 409).body(result);
    }

    // 4. Comment List Pagination (ADMIN)
    @GetMapping("/pagination")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<PageImpl<CommentResponseDto>> paginationComment(@RequestParam(name = "page",defaultValue = "1") Integer page,
                                                                   @RequestParam(name = "size",defaultValue = "3") Integer size) {
        PageImpl<CommentResponseDto> responseDtoPage=commentService.paginationComment(page-1,size);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoPage);
    }

    //  5. Comment List By profileId(ADMIN)
    @GetMapping("/comment-list-by-profileId/{profileId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentListByProfileId(@PathVariable Integer profileId) {
        List<CommentResponseDto> responseDtoList=commentService.getCommentListByProfileId(profileId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    // 6. Comment List By Profile (murojat qilgan odamning comment lari) (USER AND OWNER)
    @GetMapping("/get-comment-list")
    public ResponseEntity<List<CommentResponseDto>> getCommentList() {
        List<CommentResponseDto> responseDtoList=commentService.getCommentList();
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    // 7. Comment List by videoId CommentInfo
    @GetMapping("/comment-list-by-videoId/{videoId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentListByVideoId(@PathVariable String videoId) {
        List<CommentResponseDto> responseDtoList=commentService.getCommentListByVideoId(videoId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }
}
