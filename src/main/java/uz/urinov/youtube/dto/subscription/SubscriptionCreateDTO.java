package uz.urinov.youtube.dto.subscription;

import lombok.Getter;
import lombok.Setter;
import uz.urinov.youtube.enums.NotificationType;
@Getter
@Setter
public class SubscriptionCreateDTO {
    //channel_id,notification_type (keladigna parametr)
    private String channelId;
    private NotificationType notificationType;
}
