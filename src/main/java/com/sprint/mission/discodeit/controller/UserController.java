package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.dto.UserRegisterResponseDto;
import com.sprint.mission.discodeit.controller.dto.UserUpdateResponseDto;
import com.sprint.mission.discodeit.controller.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.dto.UserCreateDto;
import com.sprint.mission.discodeit.service.dto.UserUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDto> register(@ModelAttribute UserCreateDto createDto) {
        User user = userService.createUser(createDto);
        UserRegisterResponseDto response = new UserRegisterResponseDto(user.getId(), user.getEmail(), user.getNickname(), user.getCreatedAt());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<UserDto>> getAll() {
        List<UserDto> userList = userService.findAll();
        return ResponseEntity.ok(userList);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserUpdateResponseDto> update(@ModelAttribute UserUpdateDto updateDto) { //@PathVariable UUID id
        User user = userService.updateUser(updateDto);
        UserUpdateResponseDto response = new UserUpdateResponseDto(user.getNickname(), user.getStatus(), user.getRole());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserStatus> updateOnline(@PathVariable UUID id) {
        UserStatus response = userStatusService.updateByUserId(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
