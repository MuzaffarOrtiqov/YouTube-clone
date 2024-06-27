package uz.urinov.youtube.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uz.urinov.youtube.entity.EmailHistoryEntity;
import uz.urinov.youtube.entity.ProfileEntity;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailHistoryRepository extends CrudRepository<EmailHistoryEntity,Integer> {

    Optional<EmailHistoryEntity> findByMessageAndEmail(String message, String email);

    Long countByEmailAndCreateDateBetween(String email, LocalDateTime from, LocalDateTime to);

    @Query("SELECT e.profile FROM EmailHistoryEntity e WHERE e.message=?1")
    Optional<ProfileEntity> findByMessage(String message);
}
