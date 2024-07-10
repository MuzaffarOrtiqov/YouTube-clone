package uz.urinov.youtube.dto.subscription;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.youtube.dto.channel.ChannelResponseDTO;
import uz.urinov.youtube.dto.profile.ProfileResponseDTO;
import uz.urinov.youtube.enums.NotificationType;
import uz.urinov.youtube.enums.SubscriptionStatus;

import java.time.LocalDateTime;
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionDTO {
    /*   id,profile_id,channel_id,created_date, unsubscribe_date,
    status (active,block),notification_type(All,Personalized,Non)*/
    private String id;
    private Integer profileId;
    private ProfileResponseDTO profileDTO;
    private String channelId;
    private ChannelResponseDTO channelDTO;
    private LocalDateTime createdDate;
    private LocalDateTime unsubscribeDate;
    private SubscriptionStatus status;
    private NotificationType notificationType;


}
