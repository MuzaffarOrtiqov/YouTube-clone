package uz.urinov.youtube.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.urinov.youtube.dto.commentLike.CommentLikeCreateDto;
import uz.urinov.youtube.dto.commentLike.CommentLikeResponseDto;
import uz.urinov.youtube.entity.CommentEntity;
import uz.urinov.youtube.entity.CommentLikeEntity;
import uz.urinov.youtube.enums.LikeStatus;
import uz.urinov.youtube.repository.CommentLikeRepository;
import uz.urinov.youtube.repository.CommentRepository;
import uz.urinov.youtube.util.SecurityUtil;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final CommentService commentService;
    private final CommentRepository commentRepository;

    // 1. Create Comment like
    public CommentLikeResponseDto addCommentLikeAndDislike(CommentLikeCreateDto dto) {

        CommentEntity comment = commentService.getComment(dto.getCommentId());

        Integer profileId = SecurityUtil.getProfileId();

        Optional<CommentLikeEntity> optionalCommentLike = commentLikeRepository.findByCommentIdAndProfileId(dto.getCommentId(), profileId);
        if (optionalCommentLike.isEmpty()) {

            CommentLikeEntity commentLikeEntity = new CommentLikeEntity();

            commentLikeEntity.setCommentId(dto.getCommentId());
            commentLikeEntity.setProfileId(profileId);
            if (dto.getStatus().equals(LikeStatus.LIKE)) {
                commentLikeEntity.setStatus(dto.getStatus());
                comment.setLikeCount(comment.getLikeCount() + 1);
                commentRepository.save(comment);
                commentLikeRepository.save(commentLikeEntity);
                return getCommentlikeResponseDto(commentLikeEntity);
            }
            commentLikeEntity.setStatus(dto.getStatus());
            comment.setDislikeCount(comment.getDislikeCount() + 1);
            commentRepository.save(comment);
            commentLikeRepository.save(commentLikeEntity);
            return getCommentlikeResponseDto(commentLikeEntity);
        }


        CommentLikeEntity commentLike = optionalCommentLike.get();


        if (commentLike.getStatus().equals(LikeStatus.LIKE) && dto.getStatus().equals(LikeStatus.LIKE)) {
            comment.setLikeCount(comment.getLikeCount() - 1);
            commentRepository.save(comment);
            commentLikeRepository.delete(commentLike);
            return getCommentlikeResponseDto(commentLike);

        }

        if (commentLike.getStatus().equals(LikeStatus.LIKE) && dto.getStatus().equals(LikeStatus.DISLIKE)) {
            commentLike.setStatus(dto.getStatus());
            comment.setDislikeCount(comment.getDislikeCount() + 1);
            comment.setLikeCount(comment.getLikeCount() - 1);
            commentRepository.save(comment);
            commentLikeRepository.save(commentLike);
            return getCommentlikeResponseDto(commentLike);
        }

        if (commentLike.getStatus().equals(LikeStatus.DISLIKE) && dto.getStatus().equals(LikeStatus.DISLIKE)) {
            comment.setDislikeCount(comment.getDislikeCount() - 1);
            commentRepository.save(comment);
            commentLikeRepository.delete(commentLike);
            return getCommentlikeResponseDto(commentLike);
        }

            comment.setDislikeCount(comment.getDislikeCount() - 1);
            comment.setLikeCount(comment.getLikeCount() + 1);
            commentLike.setStatus(dto.getStatus());
            commentRepository.save(comment);
            commentLikeRepository.save(commentLike);
            return getCommentlikeResponseDto(commentLike);

    }

    //3. User Liked Comment List (order by created_date desc) (USER)
    public List<CommentLikeResponseDto> getUserLikedCommentList() {
        Integer profileId = SecurityUtil.getProfileId();
        List<CommentLikeEntity> commentLikeEntityList = commentLikeRepository.findByProfileIdAndStatus(profileId,LikeStatus.LIKE);
        return commentLikeEntityList.stream().map(this::getCommentlikeResponseDto).toList();
    }

    // 4. Get User LikedComment List By UserId (ADMIN)
    public List<CommentLikeResponseDto> getUserLikedCommentListUserId(int userId) {
        List<CommentLikeEntity> commentLikeEntityList = commentLikeRepository.findByProfileIdAndStatus(userId,LikeStatus.LIKE);
        return commentLikeEntityList.stream().map(this::getCommentlikeResponseDto).toList();
    }


    public CommentLikeResponseDto getCommentlikeResponseDto(CommentLikeEntity entity) {
        CommentLikeResponseDto dto = new CommentLikeResponseDto();
        dto.setId(entity.getId());
        dto.setCommentId(entity.getCommentId());
        dto.setProfileId(entity.getProfileId());
        dto.setStatus(entity.getStatus());
        dto.setCreatedDate(entity.getCreateDate());
        return dto;

    }


}
