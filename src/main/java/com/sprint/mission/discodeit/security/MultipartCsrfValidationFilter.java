package com.sprint.mission.discodeit.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class MultipartCsrfValidationFilter extends OncePerRequestFilter {

  private final CsrfTokenRepository csrfTokenRepository;

  public MultipartCsrfValidationFilter(CsrfTokenRepository csrfTokenRepository) {
    this.csrfTokenRepository = csrfTokenRepository;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String method = request.getMethod();
    String requestURI = request.getRequestURI();

    log.debug("[CSRF FILTER] 요청 URI: {}", requestURI);

    if (!method.equalsIgnoreCase("POST") &&
        !method.equalsIgnoreCase("PUT") &&
        !method.equalsIgnoreCase("DELETE") &&
        !method.equalsIgnoreCase("PATCH")) {
      filterChain.doFilter(request, response);
      return;
    }

    CsrfToken expectedToken = csrfTokenRepository.loadToken(request);
    String actualToken = request.getHeader("X-CSRF-TOKEN");

    if (expectedToken == null) {
      log.warn("[CSRF FILTER] 기대 토큰 없음");
      throw new MissingCsrfTokenException("요청에 CSRF 토큰이 없습니다.");
    }

    log.debug("[CSRF FILTER] 기대 토큰: {}", expectedToken.getToken());
    log.debug("[CSRF FILTER] 실제 토큰: {}", actualToken);

    if (actualToken == null || actualToken.isBlank()) {
      throw new MissingCsrfTokenException("요청에 CSRF 토큰이 없습니다.");
    }

    if (!expectedToken.getToken().equals(actualToken)) {
      throw new InvalidCsrfTokenException(expectedToken, actualToken);
    }

    filterChain.doFilter(request, response);
  }
}