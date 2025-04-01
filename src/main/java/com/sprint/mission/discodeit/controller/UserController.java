package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.*;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.util.LogMapUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<UserDto> createUser(@RequestBody UserCreateRequest userRequest) {
        UserDto userDto = userService.create(userRequest);
        log.info("{}", LogMapUtil.of("action", "createUser")
                .add("userDto", userDto));

        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<User> updateUser(@RequestBody UserUpdateRequest userRequest) {
        User updatedUser = userService.update(userRequest);
        log.info("{}", LogMapUtil.of("action", "updateUser")
                .add("updatedUser", updatedUser));
        return ResponseEntity.ok(updatedUser);
    }

    @RequestMapping(value = "/readAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> readAllUsers() {
        List<UserDto> userDtos = userService.readAll();
        log.info("{}", LogMapUtil.of("action", "readAllUsers")
                .add("userDtos", userDtos));

        return ResponseEntity.ok(userDtos);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@RequestBody UserDeleteRequest request) {
        userService.delete(request);
        log.info("{}", LogMapUtil.of("action", "deleteUser")
                .add("userKey", request.userKey()));

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/updateStatus", method = RequestMethod.PUT)
    public ResponseEntity<UserStatus> updateUserStatus(@RequestBody UserStatusUpdateRequest request) {
        UserStatus updatedUserStatus = userStatusService.update(request);
        log.info("{}", LogMapUtil.of("action", "updateStatus")
                .add("updatedUserStatus", updatedUserStatus));

        return ResponseEntity.ok(updatedUserStatus);
    }
}
