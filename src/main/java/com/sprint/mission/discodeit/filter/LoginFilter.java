package com.sprint.mission.discodeit.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public LoginFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl("/api/auth/login");
    }

    //responde 사용 x -> 상속땜에 명시적으로 받음
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response)
        throws AuthenticationException {

        try {                               //1->2인자 타입 으로 변환
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(),
                LoginRequest.class);

            // 인증 토큰 생성
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                loginRequest.username(), loginRequest.password());
            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);

        } catch (IOException e) {
            throw new AuthenticationServiceException("JSON 파싱 실패: " + e.getMessage());
        } catch (Exception e) {
            throw new AuthenticationServiceException("인증 실패: " + e.getMessage());
        }
    }
}
