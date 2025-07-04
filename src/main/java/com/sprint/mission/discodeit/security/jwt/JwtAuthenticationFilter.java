package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.Role;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    // 1. 요청 헤더에서 JWT 토큰 추출
    String authorizationHeader = request.getHeader("Authorization");
    if (!StringUtils.hasText(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }
    String token = authorizationHeader.substring(7);

    // 2. JWT 토큰 유효성 검사
    jwtService.validateToken(token).ifPresent(claims -> {
      // 3. 토큰이 유효하면, 클레임에서 사용자 정보를 뽑아 Authentication 객체 생성
      UserDto userDto = extractUserDtoFromClaims(claims);
      Authentication authentication = new UsernamePasswordAuthenticationToken(
          userDto, // Principal 객체로 UserDto 사용
          null,
          Collections.singletonList(new SimpleGrantedAuthority(userDto.role().name()))
      );

      // 4. SecurityContext에 Authentication 객체 저장
      SecurityContextHolder.getContext().setAuthentication(authentication);
    });

    filterChain.doFilter(request, response);
  }

  // Claims에서 UserDto를 추출하는 헬퍼 메서드
  private UserDto extractUserDtoFromClaims(Claims claims) {
    LinkedHashMap<String, Object> userClaims = claims.get("user", LinkedHashMap.class);
    return new UserDto(
        UUID.fromString((String) userClaims.get("id")),
        (String) userClaims.get("username"),
        (String) userClaims.get("email"),
        null, // 프로필 정보는 토큰에 없음
        (Boolean) userClaims.get("online"),
        Role.valueOf((String) userClaims.get("role"))
    );
  }
}