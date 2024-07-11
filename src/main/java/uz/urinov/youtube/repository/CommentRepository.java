package uz.urinov.youtube.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uz.urinov.youtube.entity.CommentEntity;
import uz.urinov.youtube.mapper.CommentInfoMapper;
import uz.urinov.youtube.mapper.CommentMapper;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends CrudRepository<CommentEntity,Integer> {

    Optional<CommentEntity> findByProfileIdAndVideoId(Integer profileId, String videoId);

    Page<CommentEntity> findAll(Pageable pageable);

    // 5. Comment List By profileId(ADMIN)
    @Query(value = "SELECT c.id, c.content, c.create_date, c.like_count, c.dislike_count, c.video_id, a.origin_name, v.preview_attach_id, v.title, a.duration" +
            " FROM comment AS c" +
            " INNER JOIN video AS v ON v.id=c.video_id " +
            " INNER JOIN attach AS a ON a.id=v.attach_id where c.profile_id=?1",nativeQuery = true)
    List<CommentMapper>getCommentListByProfileId(Integer profileId);

    // 7. Comment List by videoId CommentInfo
    @Query(value = "SELECT c.id, c.content,c.create_date AS createdDate, c.like_count AS likeCount, c.dislike_count AS dislikeCount," +
            " p.id AS profileId, p.name, p.surname, p.photo_id" +
            " FROM comment AS c " +
            " INNER JOIN profile AS p ON p.id=c.profile_id" +
            " where c.video_id=?1 ORDER BY c.id;",nativeQuery = true)
    List<CommentInfoMapper>getCommentListByVideoId(String profileId);
}
