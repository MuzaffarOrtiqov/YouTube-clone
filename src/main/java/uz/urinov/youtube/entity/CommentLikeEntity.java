package uz.urinov.youtube.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.youtube.enums.LikeStatus;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "comment_like") // comment_like
public class CommentLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;

    @Column(name = "comment_id")
    private Integer commentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", insertable = false, updatable = false)
    private CommentEntity comment;

    @Enumerated(EnumType.STRING)
    private LikeStatus status;


    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();

}
