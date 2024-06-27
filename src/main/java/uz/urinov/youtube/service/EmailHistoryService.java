package uz.urinov.youtube.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.urinov.youtube.entity.EmailHistoryEntity;
import uz.urinov.youtube.exp.AppBadException;
import uz.urinov.youtube.repository.EmailHistoryRepository;
import uz.urinov.youtube.util.SecurityUtil;

import java.time.LocalDateTime;

@Service
public class EmailHistoryService {
    @Autowired
    private EmailHistoryRepository emailHistoryRepository;

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

}
