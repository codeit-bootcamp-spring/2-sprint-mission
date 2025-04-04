package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.request.UserUpdateDto;
import com.sprint.mission.discodeit.dto.user.request.UserCreateDto;
import com.sprint.mission.discodeit.dto.auth.request.UserLoginDto;
import com.sprint.mission.discodeit.dto.user.response.UserCreateResponse;
import com.sprint.mission.discodeit.dto.user.response.UserDeleteResponse;
import com.sprint.mission.discodeit.dto.user.response.UserUpdateResponse;
import com.sprint.mission.discodeit.dto.user.response.UsersResponse;
import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusUpdateResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<UsersResponse> userFindAll() {
        return ResponseEntity.ok(new UsersResponse(userService.findAll()));
    }

    @PostMapping
    public ResponseEntity<UserCreateResponse> userCreate(@RequestBody @Validated UserCreateDto userCreateDto){
        userService.create(userCreateDto);
        return ResponseEntity.ok(new UserCreateResponse(true, "회원 가입 완료되었습니다."));
    }

    @DeleteMapping
    public ResponseEntity<UserDeleteResponse> userDelete(@RequestBody @Validated UserLoginDto userLoginDto){
        User user = authService.login(userLoginDto);
        userService.delete(user.getId());
        return ResponseEntity.ok(new UserDeleteResponse(true, "회원 탈퇴 성공하였습니다."));
    }

    @PatchMapping
    public ResponseEntity<UserUpdateResponse> userUpdate(@RequestBody @Validated UserUpdateDto userUpdateDto){
        userService.update(userUpdateDto);
        return  ResponseEntity.ok(new UserUpdateResponse(true, "회원 업데이트 성공하였습니다."));
    }

    @PatchMapping("/userStatus")
    public ResponseEntity<UserStatusUpdateResponse> userStatusUpdate(@RequestBody @Validated UserStatusDto userStatusDto){
        userStatusService.update(userStatusDto);
        return  ResponseEntity.ok(new UserStatusUpdateResponse(true, "회원 상태 업데이트 성공하였습니다."));
    }

}
