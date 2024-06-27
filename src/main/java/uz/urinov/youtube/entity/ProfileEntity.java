package uz.urinov.youtube.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uz.urinov.youtube.enums.ProfileRole;
import uz.urinov.youtube.enums.ProfileStatus;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "profile")
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String surname;

    @Column(length = 50)
    private String email;

    @Column(length = 50)
    private String password;

    @Column(name = "photo_id",unique = true)
    private String photoId;

    @Column(name = "visible")
    private Boolean visible=Boolean.TRUE;

    @Enumerated(EnumType.STRING)
    private ProfileRole role;

    @Enumerated(EnumType.STRING)
    private ProfileStatus status;

    @Column(name = "create_date")
    private LocalDateTime createDate=LocalDateTime.now();

}
