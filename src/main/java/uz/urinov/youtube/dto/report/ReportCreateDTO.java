package uz.urinov.youtube.dto.report;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.youtube.entity.ProfileEntity;
import uz.urinov.youtube.enums.ReportStatus;

import java.time.LocalDateTime;

@Setter
@Getter
public class ReportCreateDTO {

    @Size(min = 3, message = "Berilgan report uzunligi 3 dan kam bo'lishi mumkin emas")
    @NotBlank(message = "Report bo'sh bo'lishi mumkin emas")
    private String content;

    @NotBlank
    private String entityId;

    @NotNull
    private ReportStatus status;

}
