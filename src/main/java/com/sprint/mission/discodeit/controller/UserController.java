package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    // [x] 사용자를 등록할 수 있다.
    @PostMapping("/create")
    public UserDto createUser(@RequestBody UserCreateRequest request) {
        return userService.create(request);
    }

    // [x] 사용자 정보를 수정할 수 있다.
    @PostMapping("/update")
    public User updateUser(@RequestBody UserUpdateRequest request) {
        return userService.update(request);
    }

    // [x] 사용자를 삭제할 수 있다.
    @PostMapping("/delete")
    public String deleteUser(@RequestBody UUID userKey) {
        userService.delete(userKey);
        return "삭제 완료";
    }

    // [x] 모든 사용자를 조회할 수 있다.
    @GetMapping("/all")
    public List<UserDto> getAllUsers() {
        return userService.readAll();
    }

    // [x] 사용자의 온라인 상태를 업데이트할 수 있다.
    @PostMapping("/status")
    public UserStatus updateUserStatus(@RequestBody UserStatusUpdateRequest request) {
        return userStatusService.update(request);
    }
}
