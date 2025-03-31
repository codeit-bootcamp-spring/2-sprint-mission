package com.sprint.mission.discodeit.controller.api;


import com.sprint.mission.discodeit.dto.UserService.*;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
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
    private final AuthService authService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserDto> create(@RequestBody UserCreateRequest request) {
        User user = userService.create(request, null); // 미완성
        UserDto userDto = convertUserToUserDto(user);
        return ResponseEntity.ok(userDto);
    }



    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> userDtos = userService.findAll().stream().map(UserController::convertUserToUserDto).toList();

        return ResponseEntity.ok(userDtos);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<UserDto> update(@PathVariable UUID userId , @RequestBody UserUpdateRequest request) {
        User user = userService.update(userId, request);
        UserDto userDto = convertUserToUserDto(user);

        return ResponseEntity.ok(userDto);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH)
    public ResponseEntity<Void> updateUserStatus(@PathVariable UUID userId) {

        userStatusService.updateByUserId(userId);

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID userId) {
        userService.delete(userId);

        return ResponseEntity.noContent().build();
    }

    private static UserDto convertUserToUserDto(User user) {
        return new UserDto(user.getId(),user.getCreatedAt(),user.getUpdatedAt(),user.getUserName(),user.getEmail(),user.getPassword(),user.getProfileId());
    }

}
