package uz.urinov.youtube.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uz.urinov.youtube.entity.ChannelEntity;

import java.util.List;
import java.util.Optional;


public interface ChannelRepository extends CrudRepository<ChannelEntity, String> {

    // 1. Create Channel (USER)
    Optional<ChannelEntity> findByProfileIdAndName(Integer profileId, String name);

    // 5. Channel Pagination (ADMIN)
    @Query(value = "FROM ChannelEntity ")
    Page<ChannelEntity> findAll(Pageable pageable);

    // 8. User Channel List (murojat qilgan userni)
    List<ChannelEntity> findByProfileId(Integer profileId);

    Optional<ChannelEntity> findByIdAndProfileId(String id, Integer profileId);
}
