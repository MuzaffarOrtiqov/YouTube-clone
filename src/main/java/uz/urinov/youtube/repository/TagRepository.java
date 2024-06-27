package uz.urinov.youtube.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uz.urinov.youtube.entity.TagEntity;

public interface TagRepository extends CrudRepository<TagEntity, Integer> {

    // Get tag by name
    @Query(value = "FROM TagEntity AS t WHERE t.name=:tagName")
    TagEntity existsByName(String tagName);
}
