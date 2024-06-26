package like.heocholi.spartaeats.global.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import like.heocholi.spartaeats.global.exception.ErrorType;
import like.heocholi.spartaeats.domain.customer.enums.UserRole;
import like.heocholi.spartaeats.domain.customer.enums.UserStatus;
import like.heocholi.spartaeats.domain.common.dto.ErrorMessage;
import like.heocholi.spartaeats.domain.customer.dto.LoginRequestDto;
import like.heocholi.spartaeats.domain.common.dto.ResponseMessage;
import like.heocholi.spartaeats.domain.customer.entity.Customer;
import like.heocholi.spartaeats.domain.customer.exception.CustomerException;
import like.heocholi.spartaeats.domain.customer.repository.CustomerRepository;
import like.heocholi.spartaeats.global.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final CustomerRepository customerRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomerRepository customerRepository) {
        this.jwtUtil = jwtUtil;
        this.customerRepository = customerRepository;
        setFilterProcessesUrl("/customers/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUserId(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    // 로그인 성공시 처리
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        Customer customer = ((UserDetailsImpl) authResult.getPrincipal()).getCustomer();
        if(customer.getUserStatus().equals(UserStatus.DEACTIVATE)){
            throw new CustomerException(ErrorType.NOT_FOUND_USER);
        }

        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRole role = ((UserDetailsImpl) authResult.getPrincipal()).getCustomer().getRole();

        String accessToken = jwtUtil.createAccessToken(username, role);
        String refreshToken = jwtUtil.createRefreshToken(username, role);

        customer.saveRefreshToken(refreshToken.substring(7));
        customerRepository.save(customer);

        response.addHeader(JwtUtil.AUTH_ACCESS_HEADER, accessToken);
        response.addHeader(JwtUtil.AUTH_REFRESH_HEADER, refreshToken);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(ResponseMessage.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("로그인 성공")
                .data(username)
                .build())
        );
        response.getWriter().flush();
    }

    // 로그인 실패시 처리
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        ErrorType errorType = ErrorType.NOT_FOUND_AUTHENTICATION_INFO;
        response.setStatus(errorType.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(ErrorMessage.<String>builder()
                .statusCode(errorType.getHttpStatus().value())
                .message(errorType.getMessage())
                .build())
        );
        response.getWriter().flush();
    }
}
