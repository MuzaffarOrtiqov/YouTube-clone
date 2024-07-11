package uz.urinov.youtube.repository;


import org.springframework.data.repository.CrudRepository;
import uz.urinov.youtube.dto.commentLike.CommentLikeResponseDto;
import uz.urinov.youtube.entity.CommentLikeEntity;
import uz.urinov.youtube.enums.LikeStatus;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends CrudRepository<CommentLikeEntity,Integer> {

    // 1. Comment like and dislike
    Optional<CommentLikeEntity> findByCommentIdAndProfileId(Integer commentId, Integer profileId);

    //3. User Liked Comment List (order by created_date desc) (USER)
    List<CommentLikeEntity> findByProfileIdAndStatus(Integer profileId, LikeStatus status);
}

