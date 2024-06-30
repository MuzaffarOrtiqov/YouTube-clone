package uz.urinov.youtube.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.urinov.youtube.dto.ProfileCreateDTO;
import uz.urinov.youtube.dto.ProfileResponseDTO;
import uz.urinov.youtube.dto.ProfileUpdateDto;
import uz.urinov.youtube.dto.attach.AttachDTO;
import uz.urinov.youtube.entity.ProfileEntity;
import uz.urinov.youtube.exp.AppBadException;
import uz.urinov.youtube.repository.ProfileRepository;
import uz.urinov.youtube.util.MD5Util;
import uz.urinov.youtube.util.Result;
import uz.urinov.youtube.util.SecurityUtil;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private AttachService attachService;

    //	1. Change password
    public Result changePassword(String oldPassword, String newPassword) {
        ProfileEntity profileEntity = SecurityUtil.getProfile();
        String checkOldPassword = MD5Util.getMD5(oldPassword);
        if (!checkOldPassword.equals(profileEntity.getPassword())) {
            throw new AppBadException("Old password does not match");
        }
        String password = MD5Util.getMD5(newPassword);
        profileEntity.setPassword(password);
        profileRepository.save(profileEntity);
        return new Result("Password o'zgartirildi", true);
    }

    // 2. Update Email (with email verification)
    public Result updateEmail(String newEmail) {
        Optional<ProfileEntity> profileEntityOptional = profileRepository.findByEmail(newEmail);
        if (profileEntityOptional.isPresent()) {
            throw new AppBadException("Email already exists");
        }
        String emailCode = UUID.randomUUID().toString();
        authService.updateSendEmail(newEmail, emailCode);
        return new Result("Email cod confirm", true);
    }

    // 3. Update Profile Detail(name,surname)
    public Result updateDetail(ProfileUpdateDto profileUpdateDto) {
        ProfileEntity profileEntity = SecurityUtil.getProfile();
        if (!profileUpdateDto.getName().isBlank()) {
            profileEntity.setName(profileUpdateDto.getName());
        }
        if (!profileUpdateDto.getSurname().isBlank()) {
            profileEntity.setSurname(profileUpdateDto.getSurname());
        }
        profileRepository.save(profileEntity);
        return new Result("Profile updated", true);
    }

    // 4. Update Profile Attach (main_photo) (delete old attach) 2
    public Result updateProfilePhoto(MultipartFile file) {
        AttachDTO attachDTO = attachService.saveAttach(file);
        ProfileEntity profileEntity = SecurityUtil.getProfile();
        if (profileEntity.getPhotoId()!=null){
            attachService.delete(profileEntity.getPhotoId());
        }
        profileEntity.setPhotoId(attachDTO.getId());
        profileRepository.save(profileEntity);
        return new Result("Profile updated", true);
    }

    // 5. Get Profile Detail (id,name,surname,email,main_photo((url)))
    public PageImpl<ProfileResponseDTO> getProfilePage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<ProfileEntity> profileEntityPage = profileRepository.findByVisibleTrueOrderByCreateDate(pageable);
        List<ProfileResponseDTO> profileResponseDTOList = profileEntityPage.stream().map(this::getProfileResponseDTO).toList();
        return new PageImpl<>(profileResponseDTOList, pageable, profileEntityPage.getTotalElements());
    }
    //  6. Create Profile (ADMIN)
    public Result createProfileAdm(ProfileCreateDTO profileCreateDTO) {
        ProfileEntity profileEntity = new ProfileEntity();
        profileEntity.setName(profileCreateDTO.getName());
        profileEntity.setSurname(profileCreateDTO.getSurname());
        profileEntity.setEmail(profileCreateDTO.getEmail());
        profileEntity.setPassword(MD5Util.getMD5(profileCreateDTO.getPassword()));
        profileEntity.setStatus(profileCreateDTO.getStatus());
        profileEntity.setRole(profileCreateDTO.getRole());
        profileRepository.save(profileEntity);
        return new Result("Profile role "+profileEntity.getRole()+" created", true);
    }

    public ProfileResponseDTO getProfileResponseDTO(ProfileEntity profileEntity) {
        ProfileResponseDTO profileResponseDTO = new ProfileResponseDTO();
        profileResponseDTO.setId(profileEntity.getId());
        profileResponseDTO.setName(profileEntity.getName());
        profileResponseDTO.setSurname(profileEntity.getSurname());
        profileResponseDTO.setEmail(profileEntity.getEmail());
        profileResponseDTO.setRole(profileEntity.getRole().toString());
        profileResponseDTO.setStatus(profileEntity.getStatus().toString());
        profileResponseDTO.setCreateDate(profileEntity.getCreateDate());
        return profileResponseDTO;
    }


}
