package uz.urinov.youtube.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import uz.urinov.youtube.dto.auth.LoginDto;
import uz.urinov.youtube.dto.profile.ProfileCreateDTO;
import uz.urinov.youtube.dto.profile.ProfileResponseDTO;
import uz.urinov.youtube.entity.EmailHistoryEntity;
import uz.urinov.youtube.entity.ProfileEntity;
import uz.urinov.youtube.enums.ProfileRole;
import uz.urinov.youtube.enums.ProfileStatus;
import uz.urinov.youtube.exp.AppBadException;
import uz.urinov.youtube.repository.EmailHistoryRepository;
import uz.urinov.youtube.repository.ProfileRepository;
import uz.urinov.youtube.util.JWTUtil;
import uz.urinov.youtube.util.MD5Util;
import uz.urinov.youtube.util.Result;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private EmailHistoryService emailHistoryService;
    @Autowired
    private EmailHistoryRepository emailHistoryRepository;

    // 1.Profile registration Email
    public Result registrationEmail(ProfileCreateDTO dto) {
        Optional<ProfileEntity> profileEntityOptional = profileRepository.findByEmail(dto.getEmail());
        if (profileEntityOptional.isPresent()) {
//            log.warn("Email already exists email : {}", dto.getEmail());
            return new Result("Bunday telefon yoki email oldin ro'yxatga olingan", false);
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setEmail(dto.getEmail());
        entity.setPassword(MD5Util.getMD5(dto.getPassword()));
        entity.setCreateDate(LocalDateTime.now());
        entity.setRole(ProfileRole.ROLE_USER);
        entity.setStatus(ProfileStatus.INACTIVE);
        profileRepository.save(entity);

//         Emailga sms yuborish methodini chaqiramiz;

        String emailCode = UUID.randomUUID().toString();
        sendEmail(entity.getEmail(), emailCode);
        return new Result("Muvaffaqiyatli ro'yxatdan o'tdingiz. Akkounting ACTIVE qilish uchun email code tasdiqlang", true);

    }

    // 2.Profile verifyEmail
    public Result verifyEmail(String emailCode, String email) {
        Optional<EmailHistoryEntity> emailHistoryEntityOptional = emailHistoryRepository.findByMessageAndEmail(emailCode, email);
        if (emailHistoryEntityOptional.isEmpty()) {
            return new Result("Email yoki emailCode xato", false);
        }
        Optional<ProfileEntity> profileEntityOptional = profileRepository.findByEmail(email);

        if (profileEntityOptional.isEmpty()) {
            Optional<ProfileEntity> optionalProfile = emailHistoryRepository.findByMessage(emailCode);
            if (optionalProfile.isEmpty()) {
                throw new AppBadException("Email yoki emailCode xato");
            }
            ProfileEntity profileEntity = optionalProfile.get();
            profileEntity.setStatus(ProfileStatus.ACTIVE);
            profileEntity.setEmail(email);
            profileRepository.save(profileEntity);
            return new Result("Emailingiz o'zgartirildi", true);
        }
        ProfileEntity profileEntity = profileEntityOptional.get();
        profileEntity.setStatus(ProfileStatus.ACTIVE);
        profileRepository.save(profileEntity);
        return new Result("Akkound tasdiqlandi", true);
    }
    // 3.Resent Email code
    public Result verificationResendEmail(String email) {
        Optional<ProfileEntity> profileEntityOptional = profileRepository.findByEmailAndVisibleTrue(email);
        if (profileEntityOptional.isEmpty()) {
            throw new AppBadException("Email not exists");
        }
        ProfileEntity profileEntity = profileEntityOptional.get();
        if(!profileEntity.getStatus().equals(ProfileStatus.INACTIVE)) {
            throw new AppBadException("Registration not completed");
        }
        emailHistoryService.checkEmailLimit(profileEntity.getEmail());
        String emailCode = UUID.randomUUID().toString();
        sendEmail(profileEntity.getEmail(), emailCode);
        return new Result("To complete your registration please verify your email.",true);
    }

    // 3.Profile login
    public ProfileResponseDTO loginProfile(LoginDto loginDto) {
        String password = MD5Util.getMD5(loginDto.getPassword());
        Optional<ProfileEntity> profileEntityOptional = profileRepository.findByEmailAndPasswordAndVisibleTrue(loginDto.getUsername(), password);
        if (profileEntityOptional.isEmpty()) {
            throw new AppBadException("Email or password not exists");
        }
        ProfileEntity profileEntity = profileEntityOptional.get();
        ProfileResponseDTO profileResponseDTO = new ProfileResponseDTO();
        profileResponseDTO.setId(profileEntity.getId());
        profileResponseDTO.setEmail(profileEntity.getEmail());
        profileResponseDTO.setRole(profileEntity.getRole().toString());
        profileResponseDTO.setStatus(profileEntity.getStatus().toString());
        profileResponseDTO.setJwt(JWTUtil.encode(profileEntity.getId(),profileEntity.getEmail(),profileEntity.getRole()));
        return profileResponseDTO;
    }

    // Profile sendEmail
    public void sendEmail(String email, String emailCode) {
        try {
            MimeMessage msg=javaMailSender.createMimeMessage();
            msg.setFrom("youtube@gmail.com");
            MimeMessageHelper helper =null;
            helper=new MimeMessageHelper(msg,true);
            helper.setTo(email);
            helper.setSubject("Accountni tasdiqlang");

            String formatText = "<style>\n" +
                    "    a:link, a:visited {\n" +
                    "        background-color: #f44336;\n" +
                    "        color: white;\n" +
                    "        padding: 14px 25px;\n" +
                    "        text-align: center;\n" +
                    "        text-decoration: none;\n" +
                    "        display: inline-block;\n" +
                    "    }\n" +
                    "\n" +
                    "    a:hover, a:active {\n" +
                    "        background-color: red;\n" +
                    "    }\n" +
                    "</style>\n" +
                    "<div style=\"text-align: center\">\n" +
                    "    <h1>Welcome to youtube web portal</h1>\n" +
                    "    <br>\n" +
                    "    <p>Please button lick below to complete registration</p>\n" +
                    "    <div style=\"text-align: center\">\n" +
                    "        <a href=\"%s\" target=\"_blank\">This is a link</a>\n" +
                    "    </div>";
            String url = "http://localhost:8080/auth/verifyEmail?emailCode=" + emailCode + "&email=" + email;
            String text = String.format(formatText, url);
            helper.setText(text, true);
            javaMailSender.send(msg);
            emailHistoryService.createEmailHistory(emailCode, email);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSendEmail(String email, String emailCode) {
        try {
            MimeMessage msg=javaMailSender.createMimeMessage();
            msg.setFrom("youtube@gmail.com");
            MimeMessageHelper helper =null;
            helper=new MimeMessageHelper(msg,true);
            helper.setTo(email);
            helper.setSubject("Accountni tasdiqlang");

            String formatText = "<style>\n" +
                    "    a:link, a:visited {\n" +
                    "        background-color: #f44336;\n" +
                    "        color: white;\n" +
                    "        padding: 14px 25px;\n" +
                    "        text-align: center;\n" +
                    "        text-decoration: none;\n" +
                    "        display: inline-block;\n" +
                    "    }\n" +
                    "\n" +
                    "    a:hover, a:active {\n" +
                    "        background-color: red;\n" +
                    "    }\n" +
                    "</style>\n" +
                    "<div style=\"text-align: center\">\n" +
                    "    <h1>Welcome to youtube web portal</h1>\n" +
                    "    <br>\n" +
                    "    <p>Please button lick below to complete registration</p>\n" +
                    "    <div style=\"text-align: center\">\n" +
                    "        <a href=\"%s\" target=\"_blank\">This is a link</a>\n" +
                    "    </div>";
            String url = "http://localhost:8080/auth/verifyEmail?emailCode=" + emailCode + "&email=" + email;
            String text = String.format(formatText, url);
            helper.setText(text, true);
            javaMailSender.send(msg);
            emailHistoryService.updateEmailHistory(emailCode, email);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }


}
