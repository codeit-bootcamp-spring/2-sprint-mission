package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        UUID userId = userService.createUser(request).getId();
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable UUID userId, @RequestBody UpdateUserRequest request) {
        userService.updateUser(new UpdateUserRequest(
                userId,
                request.username(),
                request.password(),
                request.email(),
                request.profileImageFileName(),
                request.profileImageFilePath()
        ));
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @RequestMapping(method = RequestMethod.PATCH, path = "/{userId}/status")
    public ResponseEntity<Void> updateOnlineStatus(@PathVariable UUID userId) {
        userStatusService.updateByUserId(userId);
        return ResponseEntity.ok().build();
    }
}
