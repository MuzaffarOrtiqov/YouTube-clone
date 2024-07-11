package uz.urinov.youtube.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.youtube.enums.LikeStatus;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "video_like")
public class VideoLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;

    @Column(name = "video_id")
    private String videoId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id",insertable = false, updatable = false)
    private VideoEntity video;

    @Column(name = "created_date")
    private LocalDateTime createdDate=LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LikeStatus likeStatus;
}
