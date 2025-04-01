package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.IdResponse;
import com.sprint.mission.discodeit.dto.UserStatusUpdateResponse;
import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<IdResponse> create(@RequestBody UserCreateRequestDto userRequest) {
        User user = userService.create(userRequest);
        return ResponseEntity.ok(IdResponse.of(true, user.getId()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<IdResponse> update(
            @PathVariable("id") UUID userId,
            @RequestBody UserUpdateRequestDto request) {
        User user = userService.update(request);
        return ResponseEntity.ok(IdResponse.of(true, user.getId()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("id") UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserResponseDto>> findAll() {
        List<UserResponseDto> response = userService.findAll();
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/status/{id}", method = RequestMethod.PUT)
    public ResponseEntity<UserStatusUpdateResponse> updateStatus(
            @PathVariable("id") UUID userId, @RequestBody UserStatusUpdateRequestDto request
    ) {
        UserStatus userStatus = userStatusService.update(request);
        UserStatusUpdateResponse response = new UserStatusUpdateResponse(true, userStatus.isOnline());
        return ResponseEntity.ok(response);
    }
}
