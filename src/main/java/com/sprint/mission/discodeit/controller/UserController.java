package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.CreateUserDTO;
import com.sprint.mission.discodeit.dto.UpdateUserDTO;
import com.sprint.mission.discodeit.dto.UpdateUserStatusDTO;
import com.sprint.mission.discodeit.dto.UserResponseDTO;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    // BasicUserService가 주입됨
    private final UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody CreateUserDTO createUserDTO) {
        UserResponseDTO createUser = userService.create(createUserDTO);
        return ResponseEntity.ok(createUser);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public void updateUser(
            @PathVariable("userId") UUID userId,
            @RequestBody UpdateUserDTO updateUserDTO
    ){
        userService.update(updateUserDTO);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable("userId") UUID userId) {
        userService.delete(userId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserResponseDTO>> getAllUser() {
        List<UserResponseDTO> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @RequestMapping(value = "/{userId}/status", method = RequestMethod.PUT)
    public void updateUserStatus(@PathVariable("userId") UUID userId,
                                 @RequestBody UpdateUserStatusDTO updateUserStatusDTO) {
        UserResponseDTO updated = userService.update(new UpdateUserDTO());
    }
}
