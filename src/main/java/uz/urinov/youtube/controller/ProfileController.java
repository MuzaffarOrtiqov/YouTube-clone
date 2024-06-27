package uz.urinov.youtube.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.urinov.youtube.dto.ProfileCreateDTO;
import uz.urinov.youtube.dto.ProfileResponseDTO;
import uz.urinov.youtube.dto.ProfileUpdateDto;
import uz.urinov.youtube.service.ProfileService;
import uz.urinov.youtube.util.Result;

@RestController
@RequestMapping("/profile")
@SecurityRequirement(name = "Authorization")
@Tag(name = "Profile Controller", description = "Api list for change Password, update Email, update Detail, profile page, create ADMIN,MODERATOR ")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    //1. Profile (USER role)
    //	1. Change password
    @PutMapping("/changePassword")
    public ResponseEntity<Result> changePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        Result result = profileService.changePassword(oldPassword, newPassword);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    // 2. Update Email (with email verification)
    @PutMapping("/updateEmail")
    public ResponseEntity<Result> updateEmail(@RequestParam String newEmail) {
        Result result = profileService.updateEmail(newEmail);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    // 3. Update Profile Detail(name,surname)
    @PutMapping("/updateDetail")
    public ResponseEntity<Result> updateDetail(@RequestBody ProfileUpdateDto profileUpdateDto) {
        Result result = profileService.updateDetail(profileUpdateDto);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    // 5. Get Profile Detail (id,name,surname,email,main_photo((url)))
    @GetMapping("/profile-page")
    public ResponseEntity<PageImpl<ProfileResponseDTO>> getProfilePage(@RequestParam int page,
                                                                       @RequestParam int size) {
        PageImpl<ProfileResponseDTO> profileResponseDTOPage=profileService.getProfilePage(page-1,size);
        return ResponseEntity.status(HttpStatus.OK).body(profileResponseDTOPage);
    }
    //  6. Create Profile (ADMIN)
    @PutMapping("/adm/create")
    public ResponseEntity<Result> createProfileAdm(@RequestBody ProfileCreateDTO profileCreateDTO) {
        Result result=profileService.createProfileAdm(profileCreateDTO);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }
}
