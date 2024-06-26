package like.heocholi.spartaeats.global.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import like.heocholi.spartaeats.global.exception.ErrorType;
import like.heocholi.spartaeats.domain.customer.enums.UserRole;
import like.heocholi.spartaeats.domain.customer.entity.Customer;
import like.heocholi.spartaeats.global.exception.JwtException;
import like.heocholi.spartaeats.global.security.UserDetailsImpl;
import like.heocholi.spartaeats.global.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "Jwt 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtUtil.getAccessTokenFromHeader(request);

        if(StringUtils.hasText(accessToken)){
            if(jwtUtil.validateToken(accessToken)){
                // accessToken이 유효할 때
                authenticateWithAccessToken(accessToken);
            } else {
                // accessToken이 유효하지 않을 때
                validateAndAuthenticateWithRefreshToken(request, response);
            }
        }

        filterChain.doFilter(request, response);
    }

    // accessToken이 유효한 경우
    public void authenticateWithAccessToken(String token){
        Claims info = jwtUtil.getUserInfoFromToken(token);

        try {
            setAuthentication(info.getSubject());
        } catch (Exception e){
            log.error("username = {}, message = {}", info.getSubject(), "인증 정보를 찾을 수 없습니다.");
            throw new JwtException(ErrorType.NOT_FOUND_AUTHENTICATION_INFO);
        }
    }

    // accessToken이 유효하지 않은 경우, 리프레스 토큰 검증 및 엑세스토큰 재발급
    public void validateAndAuthenticateWithRefreshToken(HttpServletRequest request, HttpServletResponse response){
        String refreshToken = jwtUtil.getRefreshTokenFromHeader(request);

        if(StringUtils.hasText(refreshToken) && jwtUtil.validateToken(refreshToken)){
            Claims info = jwtUtil.getUserInfoFromToken(refreshToken);
            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(info.getSubject());
            Customer customer = userDetails.getCustomer();

            if(customer.validateRefreshToken(refreshToken)){
                UserRole role = customer.getRole();
                String newAccessToken = jwtUtil.createAccessToken(info.getSubject(), role);
                jwtUtil.setHeaderAccessToken(response, newAccessToken);

                try{
                    setAuthentication(info.getSubject());
                } catch (Exception e) {
                    log.error(e.getMessage());
                    throw new JwtException(ErrorType.NOT_FOUND_AUTHENTICATION_INFO);
                }
            } else {
                throw new JwtException(ErrorType.INVALID_REFRESH_TOKEN);
            }
        }
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
