package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("로그인 실패 또는 인증 안됨");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("로그인 요청 수신: principal={}", authentication.getName());
        UserDto user = authService.getCurrentUser(authentication);
        log.debug("로그인 응답: {}", user);
        return ResponseEntity.ok(user);
    }

    @GetMapping(path = "csrf-token")
    public ResponseEntity<CsrfToken> getCsrfToken(@RequestAttribute CsrfToken csrfToken) {
        log.info("CSRF 토큰 발급 요청");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(csrfToken);
    }
}
