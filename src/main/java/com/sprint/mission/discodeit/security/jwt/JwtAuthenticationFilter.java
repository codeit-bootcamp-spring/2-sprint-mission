package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.domain.auth.entity.DiscodeitUserDetails;
import com.sprint.mission.discodeit.domain.user.dto.UserDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  // 제외할 경로를 저장할 리스트 추가
  private final List<RequestMatcher> excludedMatchers;

  public JwtAuthenticationFilter(JwtService jwtService, ObjectMapper objectMapper,
      List<String> excludedPaths) {
    this.jwtService = jwtService;
    // 문자열 경로를 AntPathRequestMatcher로 변환
    this.excludedMatchers = excludedPaths.stream()
        .map(AntPathRequestMatcher::new)
        .collect(Collectors.toList());
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    // 현재 요청이 제외 경로에 해당하면 true를 반환하여 필터링을 건너뜀
    return excludedMatchers.stream().anyMatch(matcher -> matcher.matches(request));
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain
  ) throws IOException, ServletException {
    // 1. 헤더에서 토큰을 가져옵니다.
    resolveAccessToken(request)
        // 2. 토큰이 유효한 경우에만 인증 처리를 합니다.
        .filter(jwtService::validate)
        // 3. 토큰을 파싱하여 Authentication 객체를 생성하고 SecurityContext에 저장합니다.
        .ifPresent(token -> {
          UserDto userDto = jwtService.parse(token).userDto();
          DiscodeitUserDetails userDetails = new DiscodeitUserDetails(userDto, null);
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
              userDetails,
              null,
              userDetails.getAuthorities()
          );
          SecurityContextHolder.getContext().setAuthentication(authentication);
        });
    // 4. 인증 처리 여부와 관계없이 항상 다음 필터로 체인을 계속 진행합니다.
    //    (인증이 필요한 경로는 Spring Security의 후속 필터가 막아줄 것입니다)
    chain.doFilter(request, response);

  }

  private Optional<String> resolveAccessToken(HttpServletRequest request) {
    String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      return Optional.of(authorizationHeader.substring(7));
    }
    return Optional.empty();
  }
}