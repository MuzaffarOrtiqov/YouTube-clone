package uz.urinov.youtube.dto.channel;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChannelCreateDTO {

    @Size(min = 3, max = 50, message = "Berilgan name uzunligi 3 va 50 orasida bo'lishi kerak")
    @NotBlank(message = "name bo'sh bo'lishi mumkin emas")
    private String name;

    @Size(min = 3,  message = "Berilgan name uzunligi 3 va 50 orasida bo'lishi kerak")
    @NotBlank(message = "description bo'sh bo'lishi mumkin emas")
    private String description;

    private String bannerId;

    private String photoId;

}
