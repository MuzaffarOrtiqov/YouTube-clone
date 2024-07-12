package uz.urinov.youtube.dto.emailHistory;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class EmailHistoryResponseDTO {

    private Integer id;
    private String message;
    private String email;

    private Integer profileId;
    private LocalDateTime createDate;

}
