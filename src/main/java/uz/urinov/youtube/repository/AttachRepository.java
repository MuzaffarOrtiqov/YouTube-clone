package uz.urinov.youtube.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uz.urinov.youtube.entity.AttachEntity;

public interface AttachRepository extends CrudRepository<AttachEntity,String> {
    @Query(value = "FROM AttachEntity")
    Page<AttachEntity> findAll(Pageable pageable);
}
