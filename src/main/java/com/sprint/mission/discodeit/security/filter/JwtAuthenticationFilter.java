package com.sprint.mission.discodeit.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.core.auth.service.CustomUserDetailsService;
import com.sprint.mission.discodeit.core.user.dto.UserDto;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.security.jwt.JwtSessionRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final CustomUserDetailsService userDetailsService;
  private final ObjectMapper objectMapper;
  private final JwtSessionRepository jwtSessionRepository;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {
    String authorization = request.getHeader("Authorization");

    if (authorization == null || !authorization.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String accessToken = authorization.substring(7);

    Claims claims = jwtService.getClaims(accessToken);

    if (claims != null) {
      Map<String, Object> userDtoMap = claims.get("userDto", Map.class);
      UserDto userDto = objectMapper.convertValue(userDtoMap, UserDto.class);

      //DB에 세션이 있는지 확인
      //세션 없거나 토큰 불일치 시 인증 절차 중단
      boolean isTokenValidInDb = jwtSessionRepository.findByUserId(userDto.id())
          .map(session -> session.getAccessToken().equals(accessToken))
          .orElse(false);

      if (isTokenValidInDb) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userDto.username());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }
    
    filterChain.doFilter(request, response);
  }
}