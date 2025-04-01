package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.LoginResponse;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.exception.AuthException;
import com.sprint.mission.discodeit.exception.InvalidRequestException;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto.Create userDto) {

        UserDto.Response createdUser = userService.createdUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto.Login loginDto) {
        try {
            String token = authService.login(loginDto);
            return ResponseEntity.ok(new LoginResponse(loginDto.getEmail(), "true", token));
        } catch (Exception e) {
            throw new AuthException("로그인에 실패했습니다. 이메일과 비밀번호를 확인해주세요.");
        }
    }
}
