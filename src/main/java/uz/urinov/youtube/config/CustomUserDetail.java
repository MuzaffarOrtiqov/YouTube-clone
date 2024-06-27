package uz.urinov.youtube.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.urinov.youtube.entity.ProfileEntity;
import uz.urinov.youtube.enums.ProfileStatus;


import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class CustomUserDetail implements UserDetails {

    private ProfileEntity profile;

    public CustomUserDetail(ProfileEntity profile) {

        this.profile = profile;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<SimpleGrantedAuthority> roleList = new LinkedList<>();
        roleList.add(new SimpleGrantedAuthority(profile.getRole().name()));
        return roleList;
    }

    @Override
    public String getPassword() {
        return profile.getPassword();
    }

    @Override
    public String getUsername() {
        return profile.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return profile.getStatus().equals(ProfileStatus.ACTIVE);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return profile.getVisible();
    }

    public ProfileEntity getProfile() {
        return profile;
    }
}
