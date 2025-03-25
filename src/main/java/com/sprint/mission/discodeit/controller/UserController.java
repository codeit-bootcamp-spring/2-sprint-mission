package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 사용자 생성 (프로필 이미지 업로드 가능)
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserCreateRequest userCreateRequest,
                                           @RequestParam Optional<BinaryContentCreateRequest> profileCreateRequest) {
        User user = userService.create(userCreateRequest, profileCreateRequest);
        return ResponseEntity.ok(user);
    }

    // 특정 사용자 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable UUID userId) {
        UserDto userDto = userService.find(userId);
        return ResponseEntity.ok(userDto);
    }

    // 모든 사용자 조회
    @GetMapping("/findAll")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    // 사용자 정보 수정 (프로필 이미지 업데이트 가능)
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable UUID userId,
                                           @RequestBody UserUpdateRequest userUpdateRequest,
                                           @RequestParam Optional<BinaryContentCreateRequest> profileCreateRequest) {
        User updatedUser = userService.update(userId, userUpdateRequest, profileCreateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    // 사용자 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.ok("사용자가 삭제되었습니다.");
    }
}

