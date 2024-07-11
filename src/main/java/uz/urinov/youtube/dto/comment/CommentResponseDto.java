package uz.urinov.youtube.dto.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.youtube.dto.attach.AttachDTO;
import uz.urinov.youtube.dto.profile.ProfileResponseDTO;
import uz.urinov.youtube.dto.video.VideoDTO;

import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponseDto {

    private Integer id;
    private Integer profileId;
    private String videoId;
    private String content;
    private Integer like;
    private Integer dislike;
    private VideoDTO video;
    private AttachDTO attach;
    private ProfileResponseDTO profile;
    private Integer replyId;
    private LocalDateTime uploadTime;
    private LocalDateTime commentTime;
    private Boolean visible;
}
