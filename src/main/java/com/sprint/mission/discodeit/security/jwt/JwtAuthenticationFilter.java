package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ErrorResponse;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.security.SecurityMatchers;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final ObjectMapper objectMapper;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain
  ) throws IOException, ServletException {
    Optional<String> optionalAccessToken = resolveAccessToken(request);
    if (optionalAccessToken.isPresent() && !isPermitAll(request)) {
      String accessToken = optionalAccessToken.get();
      if (jwtService.validate(accessToken)) {
        UserDto userDto = jwtService.parse(accessToken).userDto();
        DiscodeitUserDetails userDetails = new DiscodeitUserDetails(userDto, null);
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);

        chain.doFilter(request, response);

      } else {
        jwtService.invalidateJwtSession(accessToken);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse errorResponse = new ErrorResponse(
            new DiscodeitException(ErrorCode.INVALID_TOKEN,
                Map.of("accessToken", accessToken)), HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
      }
    } else {
      chain.doFilter(request, response);
    }
  }

  private Optional<String> resolveAccessToken(HttpServletRequest request) {
    String prefix = "Bearer ";
    Optional<String> tokenFromHeader = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
        .filter(value -> value.startsWith(prefix))
        .map(value -> value.substring(prefix.length()));

    // 2. 헤더에 토큰이 없으면, 'token'이라는 쿼리 파라미터에서 찾기
    if (tokenFromHeader.isPresent()) {
      return tokenFromHeader;
    } else {
      return Optional.ofNullable(request.getParameter("token"));
    }
  }

  private boolean isPermitAll(HttpServletRequest request) {
    return Arrays.stream(SecurityMatchers.PUBLIC_MATCHERS)
        .anyMatch(requestMatcher -> requestMatcher.matches(request));
  }
}