package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final FileUserRepository fileUserRepository;

    public UserController(UserService userService, FileUserRepository fileUserRepository) {
        this.userService = userService;
        this.fileUserRepository = fileUserRepository;
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserCreateRequest request) {
        User user = userService.create(request, Optional.empty());
        return UserDto.from(user);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable UUID id, @RequestBody UserUpdateRequest request) {
        User user = userService.update(id, request, Optional.empty());
        return UserDto.from(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.delete(id);
    }

    @GetMapping
    public List<UserDto> findAllUsers() {
        return userService.findAll();
    }

    @PatchMapping("/{id}/status")
    public UserDto updateUserStatus(@PathVariable UUID id, @RequestBody UserStatusUpdateRequest request) {
        User user = fileUserRepository.findById(id).orElseThrow(()-> new RuntimeException("User with id " + id + " not found"));
        if (request.online() != null) {
            user.setOnline(request.online());
        }

        return UserDto.from(user);
    }
}
