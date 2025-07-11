package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.security.UserPrincipal;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessTokenAuthFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UserService userService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String token = resolveToken(request);

    if (StringUtils.hasText(token)) {
      Optional<UserDto> userDtoOpt = jwtService.getUserDto(token);

      if (userDtoOpt.isPresent()) {
        UserDto userDto = userDtoOpt.get();

        User user = userService.findById(userDto.id())
            .orElseThrow(() -> new RuntimeException("User not found"));

        UserPrincipal userPrincipal = new UserPrincipal(user);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userPrincipal,
            null,
            Collections.singleton(new SimpleGrantedAuthority(userDto.role().getAuthority()))
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("Authenticated user: {}, authorities: {}", userDto.username(), authentication.getAuthorities());
      } else {
        log.warn("Invalid access token provided");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return;
      }
    }

    filterChain.doFilter(request, response);
  }

  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

}
