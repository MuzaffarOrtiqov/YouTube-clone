package uz.urinov.youtube.dto.report;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.youtube.dto.profile.ProfileResponseDTO;
import uz.urinov.youtube.enums.ReportStatus;

import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportResponseDTO {
    private Integer id;
    private String content;
    private String entityId;
    private Integer profileId;
    private String status;
    private LocalDateTime createdDate;
    private ProfileResponseDTO profile;

}
