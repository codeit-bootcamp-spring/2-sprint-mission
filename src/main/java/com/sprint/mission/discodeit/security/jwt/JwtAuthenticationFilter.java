package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.exception.user.InvalidCredentialsException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final ObjectMapper objectMapper;

  @Override
  protected void doFilterInternal(@NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain) throws ServletException, IOException {
    try {
      String jwt = extractJwtFromRequest(request);

      if (jwt != null) {
        validateAndProcessToken(jwt, request);
      }
    } catch (Exception e) {
      logger.error("JWT 인증 처리 중 예상치 못한 오류 발생", e);
      request.setAttribute("exception", InvalidCredentialsException.class);
    }

    filterChain.doFilter(request, response);
  }

  private String extractJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");

    if (bearerToken == null) {
      return null;
    }

    if (!bearerToken.startsWith("Bearer ")) {
      throw InvalidCredentialsException.invalidUser();
    }

    String token = bearerToken.substring(7);

    if (token.trim().isEmpty()) {
      throw InvalidCredentialsException.invalidUser();
    }

    return token;
  }

  private void validateAndProcessToken(String jwt, HttpServletRequest request) {
    try {

      if (!jwtService.validateToken(jwt)) {
        throw InvalidCredentialsException.invalidUser();
      }

      String username = jwtService.extractClaims(jwt).getSubject();
      String role = jwtService.extractClaims(jwt).get("role", String.class);

      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        List<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + role));

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(username, null, authorities);

        authentication.setDetails(
            new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception e) {
      throw InvalidCredentialsException.invalidUser();
    }
  }
}
