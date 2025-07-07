package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
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

    @GetMapping(path = "csrf-token")
    public ResponseEntity<CsrfToken> getCsrfToken(CsrfToken csrfToken) {
        log.info("CSRF 토큰 발급 요청");

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(csrfToken);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(Authentication authentication) {
        log.info("현재 사용자 조회 요청");

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("현재 사용자 정보 없음 (비로그인 상태)");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDto userDto = authService.getCurrentUser(authentication);
        log.debug("현재 사용자 정보 반환: {}", userDto);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/role")
    public ResponseEntity<UserDto> updateUserRole(@RequestBody RoleUpdateRequest request) {
        log.info("사용자 권한 수정 API 요청 수신: userId={}, newRole={}", request.userId(), request.newRole());
        UserDto updatedUser = authService.updateUserRole(request);

        log.info("사용자 권한 수정 완료: userId={}, newRole={}", request.userId(), request.newRole());
        return ResponseEntity.ok(updatedUser);
    }
}
