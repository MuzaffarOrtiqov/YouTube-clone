package uz.urinov.youtube.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.urinov.youtube.dto.auth.LoginDto;
import uz.urinov.youtube.dto.profile.ProfileCreateDTO;
import uz.urinov.youtube.dto.profile.ProfileResponseDTO;
import uz.urinov.youtube.service.AuthService;
import uz.urinov.youtube.util.Result;


@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Controller", description = "Api list for authorization, registration and other ... ")
public class AuthController {
    @Autowired
    private AuthService authService;

//    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    // 1.Profile registration Email
    @PostMapping("/registrationEmail")
    public ResponseEntity<Result> registrationEmail(@Valid @RequestBody ProfileCreateDTO dto) {
        Result result = authService.registrationEmail(dto);
        return ResponseEntity.ok().body(result);
    }

    // 2.Profile verifyEmail
    @GetMapping("/verifyEmail")
    public ResponseEntity<Result> verifyEmail(@RequestParam String emailCode, @RequestParam String email) {
        Result result = authService.verifyEmail(emailCode, email);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    //3.Resent Email code
    @GetMapping("/verification/resendEmail/{email}")
    public ResponseEntity<Result> verificationResendEmail(@PathVariable String email) {
        Result result = authService.verificationResendEmail(email);
        return ResponseEntity.status(result.isSuccess() ? 200 : 409).body(result);
    }

    // 3.Profile login
    @PostMapping("/login")
    public ResponseEntity<ProfileResponseDTO> loginUser(@RequestBody LoginDto loginDto) {
        ProfileResponseDTO result = authService.loginProfile(loginDto);
        return ResponseEntity.ok().body(result);
    }

}
