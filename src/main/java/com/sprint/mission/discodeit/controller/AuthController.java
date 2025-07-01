package com.sprint.mission.discodeit.controller;

//import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

  @PostMapping(path = "/login")
  public ResponseEntity<UserDto> login(@RequestBody @Valid LoginRequest loginRequest,
                                       HttpServletRequest request) {
    log.info("로그인 요청: {}", loginRequest.username());

    try {
      UserDto user = authService.login(loginRequest, request);
      log.info("로그인 성공: {}", user.username());
      return ResponseEntity.ok(user);
    } catch (Exception e) {
      log.error("로그인 실패", e);
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

  @GetMapping("/me")
  public ResponseEntity<UserDto> getCurrentUser(
          @AuthenticationPrincipal UserDetails userDetails) {

    if (userDetails == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    try{
      User user = userRepository.findByEmail(userDetails.getUsername())
              .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을수가 없습니다."));
      UserDto dto = userMapper.toDto(user);
      return ResponseEntity.ok(dto);
    }catch (Exception e){
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletRequest request) {
    try {
      authService.logout(request);

    } catch (Exception e) {
      log.error("로그아웃 실패", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    return ResponseEntity.ok().build();
  }
}
