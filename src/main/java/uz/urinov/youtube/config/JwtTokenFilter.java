package uz.urinov.youtube.config;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.urinov.youtube.dto.JwtDTO;
import uz.urinov.youtube.util.JWTUtil;

import java.io.IOException;
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
           filterChain.doFilter(request, response);
            return;
        }
        try {
            final String token = authHeader.substring(7).trim();
            JwtDTO jwtDTO= JWTUtil.decode(token);

            String username = jwtDTO.getUsername();
            UserDetails userDetails=customUserDetailService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken
                    authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);

        }catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

    }
}
