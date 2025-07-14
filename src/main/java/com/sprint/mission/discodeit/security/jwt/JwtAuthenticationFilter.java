package com.sprint.mission.discodeit.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  // 토큰 인증이 필요한 요청인지 판단하는 메서드 (예: /api/** 경로, 또는 헤더에 토큰 존재 유무)
  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    String path = request.getRequestURI();

    // 예시: 로그인, 회원가입, 공개 API 등은 인증필터 적용하지 않음
    if (path.startsWith("/api/auth") || path.startsWith("/public")) {
      return true;  // 필터 동작 안 함
    }

    // 그 외 요청은 필터 적용
    return false;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      // 인증 헤더가 없거나 Bearer 토큰이 아니면 다음 필터 진행 (인증 실패 처리 아님)
      filterChain.doFilter(request, response);
      return;
    }

    final String token = authHeader.substring(7);

    try {
      if (jwtService.isValidToken(token)) {
        // 토큰 유효하면 토큰에서 사용자 정보(예: username, roles) 추출
        String username = jwtService.extractUsername(token);
        // 사용자 권한 정보 등도 필요하면 추가 추출 가능

        // 인증 객체 생성 (권한 목록은 필요에 맞게 설정)
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(username, null,
                jwtService.extractAuthorities(token));

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // SecurityContext에 인증 정보 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 필터 체인 계속 진행
        filterChain.doFilter(request, response);

      } else {
        // 토큰 유효하지 않음 - 401 응답
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Invalid or expired token");
      }
    } catch (Exception e) {
      // 예외 발생 시 401 응답
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write("Unauthorized: " + e.getMessage());
    }
  }
}
