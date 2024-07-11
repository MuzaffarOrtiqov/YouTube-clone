package uz.urinov.youtube.dto.videolike;

import uz.urinov.youtube.dto.profile.ProfileResponseDTO;
import uz.urinov.youtube.dto.video.VideoDTO;
import uz.urinov.youtube.enums.LikeStatus;

import java.time.LocalDateTime;

public class VideoLIkeDTO {
    //  id,profile_id,video_id,created_date,type(Like,Dislike)
    private  Long id;
    private Integer profileId;
    private ProfileResponseDTO profileResponseDTO;
    private String videoId;
    private VideoDTO videoDTO;
    private LocalDateTime createdDate;
    private LikeStatus likeStatus;
}
