package uz.urinov.youtube.dto.commentLike;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.youtube.enums.LikeStatus;


import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentLikeResponseDto {
    private Integer id;
    private Integer commentId;
    private Integer profileId;
    private LikeStatus status;
    private LocalDateTime createdDate;
}
