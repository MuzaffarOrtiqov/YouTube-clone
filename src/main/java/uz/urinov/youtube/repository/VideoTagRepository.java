package uz.urinov.youtube.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uz.urinov.youtube.dto.VideoTagShortDto;
import uz.urinov.youtube.entity.VideoTagEntity;

import java.util.List;
import java.util.Optional;

public interface VideoTagRepository extends CrudRepository<VideoTagEntity,String> {
    @Query(value = "SELECT vt.tagId FROM VideoTagEntity AS vt WHERE vt.videoId=?1 ")
    List<Integer> findTagIdListByVideoId(String videoId);

    @Query(value = "select tag.id as id, tag.name as name from video_tag inner join  tag on tag.id = video_tag.tag_id where\n" +
            " video_id = ?1",nativeQuery = true)
    List<VideoTagShortDto> findAllByVideoId(String videoId);
}
