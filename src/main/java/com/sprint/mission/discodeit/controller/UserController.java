package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<User> createUser(
            @RequestPart UserCreateRequest userCreateRequest,
            @RequestPart(required = false) MultipartFile profileFile) throws IOException {

        BinaryContentCreateRequest profileCreateRequest = null;
        if (profileFile != null && !profileFile.isEmpty()) {
            profileCreateRequest = new BinaryContentCreateRequest(
                    profileFile.getOriginalFilename(),
                    profileFile.getContentType(),
                    profileFile.getBytes()
            );
        }

        User createdUser = userService.create(userCreateRequest, Optional.ofNullable(profileCreateRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<UserDto> getUser(@PathVariable UUID userId) {
        UserDto userDto = userService.find(userId);
        return ResponseEntity.ok(userDto);
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<User> updateUser(
            @PathVariable UUID userId,
            @RequestPart UserUpdateRequest userUpdateRequest,
            @RequestPart(required = false) MultipartFile profileFile) throws IOException {

        BinaryContentCreateRequest profileCreateRequest = null;
        if (profileFile != null && !profileFile.isEmpty()) {
            profileCreateRequest = new BinaryContentCreateRequest(
                    profileFile.getOriginalFilename(),
                    profileFile.getContentType(),
                    profileFile.getBytes()
            );
        }

        User updatedUser = userService.update(userId, userUpdateRequest, Optional.ofNullable(profileCreateRequest));
        return ResponseEntity.ok(updatedUser);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{userId}/status", method = RequestMethod.PATCH)
    public ResponseEntity<Void> updateOnlineStatus(@PathVariable UUID userId, @RequestBody UserStatusUpdateRequest userStatusUpdateRequest) {
         userStatusService.updateByUserId(userId, userStatusUpdateRequest);
        return ResponseEntity.ok().build();
    }
}
