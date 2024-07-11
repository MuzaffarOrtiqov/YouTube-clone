package uz.urinov.youtube.dto.commentLike;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.youtube.enums.LikeStatus;

@Setter
@Getter
public class CommentLikeCreateDto {

    @NotBlank(message = "Comment Id bo'sh bo'lishi mumkin emas")
    private Integer commentId;
    @NotNull
    private LikeStatus status;
}
