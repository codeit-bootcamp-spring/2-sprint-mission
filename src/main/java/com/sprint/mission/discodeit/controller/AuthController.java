package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.auth.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 API")
public class AuthController {

    private final UserService userService;

    @GetMapping("/csrf-token")
    public ResponseEntity<CsrfToken> getCsrfToken(
      CsrfToken csrfToken
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .header("X-CSRF-TOKEN", csrfToken.getToken())
            .body(csrfToken);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(
        @AuthenticationPrincipal DiscodeitUserDetails userDetails
    ) {
        return ResponseEntity.ok(userDetails.getUserDto());
    }

    @PutMapping("/role")
    public ResponseEntity<UserDto> updateRole(
        @RequestBody RoleUpdateRequest roleUpdateRequest
    ) {
        UserDto userDto = userService.updateRole(roleUpdateRequest);

        return ResponseEntity.ok(userDto);
    }
}