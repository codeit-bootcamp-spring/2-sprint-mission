package com.sprint.mission.discodeit.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.exception.ErrorResponse;
import com.sprint.mission.discodeit.exception.security.InvalidTokenException;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final ObjectMapper objectMapper;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
  ) throws ServletException, IOException {

    Optional<String> optionalAccessToken = resolveAccessToken(request);

    if (optionalAccessToken.isPresent() && !isPermitAll(request)) {
      String accessToken = optionalAccessToken.get();

      if (jwtService.validateToken(accessToken)) {
        UserDto userDto = jwtService.parsePayload(accessToken).userDto();
        CustomUserDetails userDetails = new CustomUserDetails(userDto, null);
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
      } else {
        jwtService.invalidateJwtSession(accessToken);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse errorResponse = new ErrorResponse(
            InvalidTokenException.invalidAccessToken(accessToken),
            HttpServletResponse.SC_UNAUTHORIZED
        );
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
      }

    } else {
      filterChain.doFilter(request, response);
    }

  }

  private Optional<String> resolveAccessToken(HttpServletRequest request) {
    String prefix = "Bearer ";
    return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
        .map(value -> {
          if (value.startsWith(prefix)) {
            return value.substring(prefix.length());
          } else {
            return null;
          }
        });
  }

  private boolean isPermitAll(HttpServletRequest request) {
    return Arrays.stream(SecurityMatchers.PUBLIC_MATCHERS)
        .anyMatch(requestMatcher -> requestMatcher.matches(request));
  }

}
