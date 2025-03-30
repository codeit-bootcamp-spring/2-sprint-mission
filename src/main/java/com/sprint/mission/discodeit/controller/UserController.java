package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.*;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public ResponseEntity<?> createUser(@RequestBody UserCreateRequest userRequest) {
        UserDto userDto = userService.create(userRequest);
        log.info("사용자 생성 완료 {}", userDto);

        return ResponseEntity.ok(userDto);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest userRequest) {
        User updatedUser = userService.update(userRequest);
        log.info("사용자 수정 완료 {}", updatedUser);

        return ResponseEntity.ok(updatedUser);
    }

    @RequestMapping(value = "/readAll", method = RequestMethod.GET)
    public ResponseEntity<?> readAllUsers() {
        List<UserDto> userDtos = userService.readAll();
        log.info("조회된 사용자 목록: {}", userDtos);

        return ResponseEntity.ok(userDtos);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@RequestBody UserDeleteRequest request) {
        userService.delete(request);
        log.info("삭제된 사용자 정보 {}", request.userKey());

        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/updateStatus", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUserStatus(@RequestBody UserStatusUpdateRequest request) {
        UserStatus updatedUserStatus = userStatusService.update(request);
        log.info("업데이트 된 사용자 상태 {}", updatedUserStatus);

        return ResponseEntity.ok(updatedUserStatus);
    }
}
