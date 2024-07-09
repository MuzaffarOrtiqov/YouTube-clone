package uz.urinov.youtube.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.youtube.enums.PlaylistStatus;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "playlist")
public class PlaylistEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_num")
    private Integer orderNum;

    @Column(name = "name",length = 50)
    private String name;

    @Column(name = "description",columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    private PlaylistStatus status;

    @Column(name = "channel_id")
    private String channelId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", insertable = false, updatable = false)
    private ChannelEntity channel;

    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;

    @Column(name = "video_count")
    private Integer videoCount;

    @Column(name = "create_date")
    private LocalDateTime created=LocalDateTime.now();

}
//  id,channel_id,name,description,status(private,public),order_num