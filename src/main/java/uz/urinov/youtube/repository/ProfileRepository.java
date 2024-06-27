package uz.urinov.youtube.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.urinov.youtube.entity.ProfileEntity;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity,Integer> {

    // 1.Profile registration
    Optional<ProfileEntity> findByEmail(String email);

    // 2.Resent Email code
    Optional<ProfileEntity>findByEmailAndVisibleTrue(String email);

    // 3.Profile login
    Optional<ProfileEntity> findByEmailAndPasswordAndVisibleTrue(String email, String password);

    // 5. Get Profile Detail (id,name,surname,email,main_photo((url)))
    Page<ProfileEntity> findByVisibleTrueOrderByCreateDate(Pageable pageable);
}
