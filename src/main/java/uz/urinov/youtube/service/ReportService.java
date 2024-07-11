package uz.urinov.youtube.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.urinov.youtube.dto.profile.ProfileResponseDTO;
import uz.urinov.youtube.dto.report.ReportCreateDTO;
import uz.urinov.youtube.dto.report.ReportResponseDTO;
import uz.urinov.youtube.entity.ReportEntity;
import uz.urinov.youtube.enums.ReportStatus;
import uz.urinov.youtube.exp.AppBadException;
import uz.urinov.youtube.mapper.ReportInfoMapper;
import uz.urinov.youtube.repository.ReportRepository;
import uz.urinov.youtube.util.Result;
import uz.urinov.youtube.util.SecurityUtil;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportRepository reportRepository;
    private final ChannelService channelService;
    @Autowired
    private VideoService videoService;

    // 1. Create repost (USER)
    public Result createReport(ReportCreateDTO dto) {
        Integer profileId = SecurityUtil.getProfileId();
        if (dto.getStatus().equals(ReportStatus.VIDEO)){
           videoService.getVideoById(dto.getEntityId());
           ReportEntity reportEntity = new ReportEntity();
           reportEntity.setEntityId(dto.getEntityId());
           reportEntity.setStatus(ReportStatus.VIDEO);
           reportEntity.setProfileId(profileId);
           reportEntity.setContent(dto.getContent());
           reportRepository.save(reportEntity);
           return new Result("Videoga report yaratildi",true);
       }
        channelService.getChannel(dto.getEntityId());
        ReportEntity reportEntity = new ReportEntity();
        reportEntity.setEntityId(dto.getEntityId());
        reportEntity.setStatus(ReportStatus.CHANNEL);
        reportEntity.setProfileId(profileId);
        reportEntity.setContent(dto.getContent());
        reportRepository.save(reportEntity);
        return new Result("Channelga report yaratildi",true);
    }

    //  2. ReportList Pagination ADMIN ReportInfo
    public PageImpl<ReportResponseDTO> pagination(int page, int size) {
        Pageable pageable= PageRequest.of(page, size);
        Page<ReportInfoMapper> mapperPage=reportRepository.findReportInfoMapper(pageable);
        List<ReportResponseDTO> dtoList = mapperPage.stream().map(this::getReport).toList();
        return new PageImpl<>(dtoList, pageable, mapperPage.getTotalElements());
    }

    // 3. Remove Report by id (ADMIN)
    public ReportResponseDTO getById(Integer id) {
        ReportEntity report = getReportId(id);
        ReportResponseDTO dto = new ReportResponseDTO();
        dto.setId(report.getId());
        dto.setContent(report.getContent());
        dto.setProfileId(report.getProfileId());
        dto.setStatus(report.getStatus().toString());
        dto.setEntityId(report.getEntityId());
        dto.setCreatedDate(report.getCreateDate());
        return dto;
    }

    // 4. Report List By User id (ADMIN) ReportInfo
    public List<ReportResponseDTO> getByUserId(Integer id) {
        List<ReportInfoMapper> mapperList = reportRepository.getByUserId(id);
        return mapperList.stream().map(this::getReport).toList();
    }

    public ReportEntity getReportId(Integer entityId) {
        Optional<ReportEntity> entityOptional = reportRepository.findById(entityId);
        if (entityOptional.isEmpty()) {
            throw new AppBadException("Bunday report yo'q");
        }
        return entityOptional.get();
    }


    public ReportResponseDTO getReport(ReportInfoMapper mapper) {
        ReportResponseDTO responseDTO = new ReportResponseDTO();
        responseDTO.setId(mapper.getId());
        responseDTO.setEntityId(mapper.getEntityId());
        responseDTO.setStatus(mapper.getStatus());
        responseDTO.setContent(mapper.getContent());
        responseDTO.setCreatedDate(mapper.getCreatedDate());

        ProfileResponseDTO profile=new ProfileResponseDTO();
        profile.setId(mapper.getProfileId());
        profile.setName(mapper.getName());
        profile.setSurname(mapper.getSurname());
        profile.setPhotoId(mapper.getPhotoId());

        responseDTO.setProfile(profile);

        return responseDTO;
    }


}
