package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ErrorResponse;
import com.sprint.mission.discodeit.exception.user.InvalidCredentialsException;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import com.sprint.mission.discodeit.security.SecurityMatchers;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
      String bearerToken = request.getHeader("Authorization");

      if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
        if (isPermitAll(request)) {
          filterChain.doFilter(request, response);
          return;
        }
        logger.error("Authorization 헤더가 없거나 형식이 잘못되어 오류가 발생");
        handleUnauthorized(response, bearerToken);
        return;
      }

      String token = bearerToken.substring(7);

      if (jwtService.validateToken(token)) {
        UserDto userDto = jwtService.getUserDtoFromAccessToken(token);
        CustomUserDetails userDetails = new CustomUserDetails(userDto, null);
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
      } else {
        jwtService.invalidateJwtSession(token);
        handleUnauthorized(response, token);
      }
    } catch (Exception e) {
      logger.error("JWT 인증 처리 중 예상치 못한 오류 발생", e);
      request.setAttribute("exception", new InvalidCredentialsException());
    }
  }

  private void handleUnauthorized(HttpServletResponse response, String token) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    Map<String, Object> errorData = token != null ? Map.of("accessToken", token) : Map.of();
    ErrorResponse errorResponse = new ErrorResponse(
        new DiscodeitException(ErrorCode.INVALID_TOKEN, errorData),
        HttpServletResponse.SC_UNAUTHORIZED);
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }

  private boolean isPermitAll(HttpServletRequest request) {
    return Arrays.stream(SecurityMatchers.PUBLIC_MATCHERS)
        .anyMatch(matcher -> matcher.matches(request));
  }
}
