package uz.urinov.youtube.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.youtube.enums.ReportStatus;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "report")
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false)
    private ProfileEntity profile;

    @Column(columnDefinition = "text")
    private String content;

    @Column(name = "entity_id")
    private String entityId;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    @Column(name = "visible")
    private Boolean visible=Boolean.TRUE;

    @Column(name = "create_date")
    private LocalDateTime createDate = LocalDateTime.now();

}
