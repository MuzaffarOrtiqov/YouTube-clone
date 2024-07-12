package uz.urinov.youtube.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uz.urinov.youtube.entity.SubscriptionEntity;
import uz.urinov.youtube.enums.NotificationType;
import uz.urinov.youtube.enums.SubscriptionStatus;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends CrudRepository<SubscriptionEntity, String> {

    @Query("FROM SubscriptionEntity as s WHERE s.channelId=?1 AND s.profileId=?2")
    Optional<SubscriptionEntity> findSubscriptionForUser(String channelId, Integer profileId);

    @Transactional
    @Modifying
    @Query("UPDATE SubscriptionEntity AS s SET s.status=?2 WHERE s.channelId=?1")
    void updateStatus(String channelId, SubscriptionStatus status);

    @Transactional
    @Modifying
    @Query("UPDATE SubscriptionEntity AS s SET s.notificationType=?2 WHERE s.channelId=?1")
    void updateNotoficationType(String channelId, NotificationType notificationType);

    @Query("FROM SubscriptionEntity AS s WHERE s.profileId=?1 AND s.status='ACTIVE'")
    List<SubscriptionEntity> findAllSubscriptions(Integer profileId);
}
