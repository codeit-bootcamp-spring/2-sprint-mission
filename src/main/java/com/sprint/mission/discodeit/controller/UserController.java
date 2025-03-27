package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserResponseDto> create(@RequestBody UserCreateRequest request) {
        User user = userService.create(request, null);
        UserStatus status = userStatusService.findByUserId(user.getId()).orElse(null);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserResponseDto(user, status));
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@PathVariable UUID userId, @RequestBody UserUpdateRequest request) {
        userService.update(userId, request, Optional.empty());
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserResponseDto>> findAll() {
        List<User> users = userService.findAll();
        List<UserResponseDto> response = new ArrayList<>();

        for (User user : users) {
            UserStatus status = userStatusService.findByUserId(user.getId()).orElse(null);
            response.add(new UserResponseDto(user, status));
        }

        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/{userId}/status", method = RequestMethod.PATCH)
    public ResponseEntity<Void> updateStatus(@PathVariable UUID userId) {
        userStatusService.updateByUserId(
                userId,
                new UserStatusUpdateRequest(Instant.now()));

        return ResponseEntity.ok().build();
    }
}
