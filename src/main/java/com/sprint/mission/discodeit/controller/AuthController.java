package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.jwt.JwtService;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.jwt.JwtSession;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final JwtService jwtService;

  @GetMapping(path = "/csrf-token")
  public CsrfToken getCsrfToken(HttpServletRequest request) {
    CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    if (csrfToken == null) {
      csrfToken = (CsrfToken) request.getAttribute("_csrf");
    }

    if (csrfToken != null) {
      log.info("CSRF 토큰 반환: {}", csrfToken.getToken());
    } else {
      log.warn("CSRF 토큰을 찾을 수 없습니다");
    }

    return csrfToken;
  }

  @GetMapping("/me")
  public ResponseEntity<String> getCurrentUser(
          @CookieValue(value = "refresh_token") String refreshToken) {

    JwtSession jwtSession = jwtService.refreshJwtSession(refreshToken);
    return ResponseEntity
            .status(HttpStatus.OK)
            .body(jwtSession.getAccessToken());
  }

  @PutMapping("/role")
  public ResponseEntity<UserDto> updateRole(@Valid @RequestBody RoleUpdateRequest request){
    UserDto updatedUser = authService.updateRole(request);
    return ResponseEntity.ok(updatedUser);
  }
}
