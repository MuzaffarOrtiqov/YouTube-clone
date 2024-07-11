package uz.urinov.youtube.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentCreateDto {

    @NotNull
    private String videoId;

    @NotBlank(message = "Content bo'sh bo'lishi mumkin emas")
    @Size(min = 1,  message = "Berilgan Content ning uzunligi 1 ta harifdan kam bo'lishi mumkin emas")
    private String content;

    private Integer replyId;

}
