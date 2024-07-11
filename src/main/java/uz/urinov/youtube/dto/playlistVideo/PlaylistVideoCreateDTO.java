package uz.urinov.youtube.dto.playlistVideo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlaylistVideoCreateDTO {
    @NotNull
    private Integer playlistId;
    @NotBlank(message = "video id bo'sh bo'lishi mumkin emas")
    private String videoId;
    @NotNull
    private Integer orderNum;

}
