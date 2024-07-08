package uz.urinov.youtube.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import uz.urinov.youtube.enums.VideoStatus;
import uz.urinov.youtube.enums.VideoType;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "video")
public class VideoEntity {
    @Id
    @UuidGenerator
    private String id;
    @Column(name = "preview_attach_id")
    private String previewAttachId;
    @Column(name = "title",columnDefinition = "text")
    private String title;
    @Column(name = "category_id")
    private Integer categoryId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",insertable = false, updatable = false)
    private CategoryEntity category;
    @Column(name = "attach_id")
    private String attachId;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attach_id",insertable = false, updatable = false)
    private AttachEntity attach;
    @Column(name = "created_date")
    private LocalDateTime createdDate=LocalDateTime.now();
    @Column(name = "published_date")
    private LocalDateTime publishedDate;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private VideoStatus status;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private VideoType type;
    @Column(name = "view_count")
    private Long viewCount;
    @Column(name = "shared_count")
    private Long sharedCount;
    @Column(name = "description",columnDefinition = "text")
    private String description;
    @Column(name = "channel_id")
    private String channelId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id",insertable = false, updatable = false)
    private ChannelEntity channel;
    @Column(name = "like_count")
    private Long likeCount;
    @Column(name = "dislike_count")
    private Long dislikeCount;
    @Column(name = "visible")
    private Boolean visible=true;



}
