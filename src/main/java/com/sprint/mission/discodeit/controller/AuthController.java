package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BaseResponseDto;
import com.sprint.mission.discodeit.dto.auth.AuthLoginDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/login")
public class AuthController {

    private final AuthService authService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BaseResponseDto> createUser(@RequestBody AuthLoginDto authLoginDto) {
        User user = authService.login(authLoginDto);
        return ResponseEntity.ok(BaseResponseDto.success(user.getId() + " 유저 로그인이 완료되었습니다."));
    }
}
