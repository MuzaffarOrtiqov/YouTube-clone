package uz.urinov.youtube.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.urinov.youtube.config.CustomUserDetail;
import uz.urinov.youtube.dto.JwtDTO;
import uz.urinov.youtube.entity.ProfileEntity;
import uz.urinov.youtube.enums.ProfileRole;
import uz.urinov.youtube.exp.AppForbiddenException;


public class SecurityUtil {

    public static JwtDTO getJwtDTO(String token) {
        String jwt = token.substring(7); // Bearer eyJhb
        JwtDTO dto = JWTUtil.decode(jwt);
        return dto;
    }

    public static JwtDTO getJwtDTO(String token, ProfileRole requiredRole) {
        JwtDTO dto = getJwtDTO(token);
        if(!dto.getRole().equals(requiredRole)){
            throw new AppForbiddenException("Method not allowed");
        }
        return dto;
    }
    public static Integer getProfileId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
        return user.getProfile().getId();
    }

    public static ProfileEntity getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
        return user.getProfile();
    }
}
