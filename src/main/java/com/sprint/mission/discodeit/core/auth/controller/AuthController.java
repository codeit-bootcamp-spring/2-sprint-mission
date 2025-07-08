package com.sprint.mission.discodeit.core.auth.controller;


import com.sprint.mission.discodeit.auth.CustomUserDetails;
import com.sprint.mission.discodeit.core.user.usecase.UserService;
import com.sprint.mission.discodeit.core.user.usecase.dto.UserDto;
import com.sprint.mission.discodeit.swagger.AuthApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Auth", description = "로그인 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

  private final UserService userService;
//  private final AuthService

  @GetMapping("/csrf-token")
  public ResponseEntity<CsrfToken> getCsrfToken(CsrfToken csrfToken) {
    log.debug("CSRF 토큰 요청");
    return ResponseEntity.status(HttpStatus.OK).body(csrfToken);
  }

  @GetMapping("me")
  public ResponseEntity<UserDto> me(@AuthenticationPrincipal CustomUserDetails userDetails) {
    UUID userId = userDetails.getUserDto().id();
    UserDto userDto = userService.findById(userId);
    return ResponseEntity.status(HttpStatus.OK).body(userDto);
  }

//  @PutMapping("role")
//  public ResponseEntity<UserDto> role(@RequestBody RoleUpdateRequest request) {
//    log.info("권한 수정 요청");
//    UserDto userDto = authService.updateRole(request);
//
//    return ResponseEntity
//        .status(HttpStatus.OK)
//        .body(userDto);
//  }
}
