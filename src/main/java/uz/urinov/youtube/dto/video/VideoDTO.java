package uz.urinov.youtube.dto.video;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.youtube.dto.videotag.VideoTagShortDto;
import uz.urinov.youtube.dto.attach.AttachDTO;
import uz.urinov.youtube.dto.category.CategoryDTO;
import uz.urinov.youtube.dto.channel.ChannelResponseDTO;
import uz.urinov.youtube.dto.playlist.PlaylistResponseDTO;
import uz.urinov.youtube.dto.profile.ProfileResponseDTO;
import uz.urinov.youtube.dto.tag.TagDTO;
import uz.urinov.youtube.enums.VideoStatus;
import uz.urinov.youtube.enums.VideoType;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoDTO {
    /*   id(uuid), preview_attach_id,title,category_id,attach_id,created_date,published_date,
      status(private,public),
     type(video,short),view_count,shared_count,description,channel_id,(like_count,dislike_count),*/
    private String id;
    private String previewAttachId;
    private AttachDTO previewAttach;
    private String title;
    private Integer categoryId;
    private CategoryDTO category;
    private String attachId;
    private AttachDTO attach;
    private LocalDateTime createdDate;
    private LocalDateTime publishedDate;
    private VideoStatus status;
    private VideoType type;
    private Long viewCount;
    private Long sharedCount;
    private String description;
    private String channelId;
    private ChannelResponseDTO channel;
    private Long likeCount;
    private Long dislikeCount;
    private List<VideoTagShortDto> tagIdList;
    private TagDTO tag;
    private String profileId;
    private ProfileResponseDTO profileResponseDTO;
    private Integer playlistId;
    private List<PlaylistResponseDTO> playlist;
}
