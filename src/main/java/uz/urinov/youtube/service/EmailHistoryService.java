package uz.urinov.youtube.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.urinov.youtube.dto.emailHistory.EmailHistoryResponseDTO;
import uz.urinov.youtube.entity.EmailHistoryEntity;
import uz.urinov.youtube.exp.AppBadException;
import uz.urinov.youtube.repository.EmailHistoryRepository;
import uz.urinov.youtube.util.SecurityUtil;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailHistoryService {
    private final EmailHistoryRepository emailHistoryRepository;

    public void createEmailHistory(String emailCode, String toEmail) {
        EmailHistoryEntity emailHistoryEntity = new EmailHistoryEntity();
        emailHistoryEntity.setMessage(emailCode);
        emailHistoryEntity.setEmail(toEmail);
        emailHistoryEntity.setCreateDate(LocalDateTime.now());
        emailHistoryRepository.save(emailHistoryEntity);

    }

    public void updateEmailHistory(String emailCode, String toEmail) {
        EmailHistoryEntity emailHistoryEntity = new EmailHistoryEntity();
        emailHistoryEntity.setMessage(emailCode);
        emailHistoryEntity.setEmail(toEmail);
        emailHistoryEntity.setCreateDate(LocalDateTime.now());
        Integer profileId = SecurityUtil.getProfileId();
        emailHistoryEntity.setProfileId(profileId);
        emailHistoryRepository.save(emailHistoryEntity);
    }

    public void checkEmailLimit(String email) {

        LocalDateTime to = LocalDateTime.now();
        LocalDateTime from = to.minusMinutes(2);

        long count = emailHistoryRepository.countByEmailAndCreateDateBetween(email,from,to);
        if(count >=3) {
            throw new AppBadException("Sms limit reached. Please try after some time");
        }
    }

    // 1. Get email pagination (ADMIN)
    public PageImpl<EmailHistoryResponseDTO> pagination(int page, int size) {
        Pageable pageable= PageRequest.of(page, size);
        Page<EmailHistoryEntity> emailHistoryEntities = emailHistoryRepository.findAllBy(pageable);
        List<EmailHistoryResponseDTO> dtoList = emailHistoryEntities.stream().map(this::getEmailHistory).toList();
        return new PageImpl<>(dtoList, pageable, emailHistoryEntities.getTotalElements());
    }


    public EmailHistoryResponseDTO getEmailHistory(EmailHistoryEntity entity) {
        EmailHistoryResponseDTO dto = new EmailHistoryResponseDTO();
        dto.setId(entity.getId());
        dto.setMessage(entity.getMessage());
        dto.setEmail(entity.getEmail());
        dto.setProfileId(entity.getProfileId());
        dto.setCreateDate(entity.getCreateDate());
        return dto;

    }
}
