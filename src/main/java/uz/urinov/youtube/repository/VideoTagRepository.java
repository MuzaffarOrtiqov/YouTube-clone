package uz.urinov.youtube.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uz.urinov.youtube.entity.VideoTagEntity;

public interface VideoTagRepository extends CrudRepository<VideoTagEntity,String> {
    @Query(value = "SELECT vt.tagId FROM VideoTagEntity AS vt WHERE vt.videoId=?1 ")
    Integer findTagIdByVideoId(String videoId);
}
