package uz.urinov.youtube.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "video_tag")
public class VideoTagEntity {
    @Id
    @UuidGenerator
    private String id;

    @Column(name = "video_id")
    private String videoId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id",insertable=false,updatable=false)
    private VideoEntity video;

    @Column(name = "tag_id")
    private Integer tagId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id",insertable=false,updatable=false)
    private TagEntity tag;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;


}
