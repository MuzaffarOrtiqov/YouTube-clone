package uz.urinov.youtube.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "playlist_video", uniqueConstraints = {@UniqueConstraint(columnNames = {"playlist_id", "video_id"})})
public class PlaylistVideoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "playlist_id")
    private Integer playlistId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id",insertable = false, updatable = false)
    private PlaylistEntity playlist;

    @Column(name = "video_id")
    private String videoId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id",insertable = false, updatable = false)
    private VideoEntity video;

    @Column(name = "order_num")
    private Integer orderNum;

    @Column(name = "create_date")
    private LocalDateTime created=LocalDateTime.now();

}
