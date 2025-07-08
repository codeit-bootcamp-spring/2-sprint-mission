package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.LoginRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@Slf4j
@RequiredArgsConstructor
public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {

        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(),
                LoginRequest.class);

            // UsernamePasswordAuthenticationToken 토큰 생성
            UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(
                    loginRequest.username(),
                    loginRequest.password()
                );

            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);

        } catch (IOException e) {
            throw new AuthenticationServiceException("로그인 요청 파싱 실패", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {

        // 1. SecurityContextHolder에 인증 정보 저장
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);

        // 2. 세션에 컨텍스트 저장
        HttpSessionSecurityContextRepository repo =
            new HttpSessionSecurityContextRepository();
        repo.saveContext(context, request, response);

        String rememberMeParam = request.getParameter("remember-me");
        if (getRememberMeServices() != null && "true".equals(rememberMeParam)) {
            getRememberMeServices().loginSuccess(request, response, authResult);
        }

        getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }
}
