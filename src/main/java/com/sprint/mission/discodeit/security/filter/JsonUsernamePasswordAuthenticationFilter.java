package com.sprint.mission.discodeit.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
@RequiredArgsConstructor
public class JsonUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final CacheManager cacheManager;

    @PostConstruct
    public void init() {
        // 경로 필터링
        setRequiresAuthenticationRequestMatcher(
            new AntPathRequestMatcher("/api/auth/login", "POST")
        );
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("[LOGIN] attemptAuthentication 시작");
        try{
            // JSON 요청 body를 loginRequest로 변환
            LoginRequest loginReqeust = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

            // username, password를 이용한 인증 토큰 객체 생성
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                loginReqeust.username(), loginReqeust.password()
            );

            // 토큰에 부가 정보 붙이고, 매니저에게 실제 인증 위임
            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (IOException e) {
            log.error("[LOGIN ERROR] Failed to parse request body: {}", e.getMessage(), e);
            throw new AuthenticationServiceException("Invalid login request format", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain,
        Authentication authResult) throws IOException, ServletException {
        log.info("[LOGIN] successfulAuthentication 시작");

        // 캐시 제거
        Cache cache = cacheManager.getCache("findAllUsers");
        if (cache != null) {
            cache.clear(); // 전체 삭제
            log.info("로그인 성공 - 사용자 캐시 제거 완료");
        }

        // 성공 핸들러 실행
        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException failed)
        throws IOException, ServletException {
        getFailureHandler().onAuthenticationFailure(request, response, failed);
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        log.info("요청 URI: {}", request.getRequestURI());
        log.info("현재 SecurityContext: {}", SecurityContextHolder.getContext().getAuthentication());
        return super.requiresAuthentication(request, response);
    }
}
