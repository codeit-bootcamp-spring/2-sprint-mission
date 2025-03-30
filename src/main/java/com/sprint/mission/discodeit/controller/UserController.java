package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.StatusDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ForbiddenException;
import com.sprint.mission.discodeit.exception.InvalidRequestException;
import com.sprint.mission.discodeit.jwt.RequiresAuth;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import org.springframework.web.bind.annotation.RequestMapping; // RequestMapping 임포트 추가

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @GetMapping("/find/{userId}")
    public ResponseEntity<UserDto.Summary> findUser(@PathVariable("userId") UUID userId) {
        return ResponseEntity.ok(userService.findByUserId(userId));
    }

    @GetMapping("/finds")
    public ResponseEntity<List<UserDto.Summary>> findAllUsers() {
        return ResponseEntity.ok(userService.findByAllUsersId());
    }

    @RequiresAuth
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<UserDto.DeleteResponse> deleteUser(
            @PathVariable UUID userId) {

        userService.deleteUser(userId);
        return ResponseEntity.ok(new UserDto.DeleteResponse(userId.toString(), "true"));
    }

    @RequiresAuth
    @PutMapping("/update/{userId}")
    public ResponseEntity<UserDto.Update> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestBody UserDto.Update updateDto) {

        return ResponseEntity.ok(userService.updateUser(userId, updateDto));
    }

    @RequiresAuth
    @PutMapping("/status/{userId}")
    public ResponseEntity<StatusDto.StatusResponse> updateUserStatus(
            @PathVariable UUID userId,@RequestBody StatusDto.StatusRequest statusRequest) {
        System.out.println("Asdadsasdasd");
        return ResponseEntity.ok(userStatusService.updateUserStatus(userId,statusRequest));
    }
}