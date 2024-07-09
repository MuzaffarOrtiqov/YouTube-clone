package uz.urinov.youtube.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import uz.urinov.youtube.enums.ChannelStatus;


import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "channel")
public class ChannelEntity {

    @Id
    @UuidGenerator
    private String id;

    @Column(length = 50)
    private String name;

    @Column(name = "photo_id")
    private String photoId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id",insertable = false, updatable = false)
    private AttachEntity photo;

    @Column(name = "banner_id")
    private String bannerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_id",insertable = false, updatable = false)
    private AttachEntity banner;

    @Column(name = "description",columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    private ChannelStatus status=ChannelStatus.ACTIVE;

    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id",insertable = false, updatable = false)
    private ProfileEntity profile;

    @Column(name = "create_date")
    private LocalDateTime createDate=LocalDateTime.now();

    

}
