package uz.urinov.youtube.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import uz.urinov.youtube.enums.NotificationType;
import uz.urinov.youtube.enums.SubscriptionStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "subscription")
public class SubscriptionEntity {
    @Id
    @UuidGenerator
    private String id;
    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id",insertable = false, updatable = false)
    private ProfileEntity profile;
    @Column(name = "channel_id")
    private String channelId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id",insertable = false, updatable = false)
    private ChannelEntity channel;
    @Column(name = "created_date")
    private LocalDateTime createdDate=LocalDateTime.now();
    @Column(name = "unsubscribe_date")
    private LocalDateTime unsubscribeDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SubscriptionStatus status=SubscriptionStatus.ACTIVE;
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType notificationType;
}
