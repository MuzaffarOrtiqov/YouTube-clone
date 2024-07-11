package uz.urinov.youtube.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uz.urinov.youtube.entity.ReportEntity;
import uz.urinov.youtube.mapper.ReportInfoMapper;

import java.util.List;

public interface ReportRepository extends CrudRepository<ReportEntity,Integer> {

    //  2. ReportList Pagination ADMIN ReportInfo
    @Query(value = "SELECT r.id, r.content, r.entity_id AS entityId, r.status, r.create_date AS createdDate," +
            " p.id AS profileId, p.name, p.surname, p.photo_id AS photoId" +
            " FROM report AS r" +
            " INNER JOIN profile AS p ON r.profile_id=p.id",nativeQuery = true)
    Page<ReportInfoMapper> findReportInfoMapper(Pageable pageable);


    // 4. Report List By User id (ADMIN) ReportInfo
    @Query(value = "SELECT r.id, r.content, r.entity_id AS entityId, r.status, r.create_date AS createdDate," +
            " p.id AS profileId, p.name, p.surname, p.photo_id AS photoId" +
            " FROM report AS r" +
            " INNER JOIN profile AS p ON r.profile_id=p.id where r.profile_id=?1",nativeQuery = true)
    List<ReportInfoMapper> getByUserId(Integer profileId);
}
