package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.user.CreateUserDTO;
import com.sprint.mission.discodeit.dto.user.UpdateUserDTO;
import com.sprint.mission.discodeit.dto.user.UserResponseDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public User createUser(@RequestBody CreateUserDTO dto, Optional<BinaryContentDTO> profileCreateRequest) {
        return userService.createUser(dto, profileCreateRequest);
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.searchAllUsers();
        return ResponseEntity.ok(users);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public UserResponseDTO getUserById(@PathVariable UUID userId) {
        return userService.searchUser(userId);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public User updateUser(@PathVariable UUID userId, @RequestBody UpdateUserDTO dto, Optional<BinaryContentDTO> profileCreateRequest) {
        return userService.updateUser(userId, dto, profileCreateRequest);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH)
    public UserResponseDTO updateOnlineState(@PathVariable UUID userId) {
        return userService.updateOnlineState(userId);
    }
}
