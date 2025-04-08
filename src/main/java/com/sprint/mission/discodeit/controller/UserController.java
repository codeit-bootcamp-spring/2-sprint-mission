package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.StatusDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserDto.Summary;
import com.sprint.mission.discodeit.dto.common.ListSummary;
import com.sprint.mission.discodeit.jwt.RequiresAuth;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @GetMapping("/{userId}")
  public ResponseEntity<Summary> findUser(@Valid @PathVariable("userId") UUID userId) {
    return ResponseEntity.ok(userService.findByUserId(userId));
  }

  @GetMapping("/all")
  public ResponseEntity<ListSummary<Summary>> findAllUsers() {
    return ResponseEntity.ok(userService.findByAllUsersId());
  }

  @RequiresAuth
  @DeleteMapping("/{userId}")
  public ResponseEntity<UserDto.DeleteResponse> deleteUser(
      @Valid @PathVariable UUID userId) {

    userService.deleteUser(userId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @RequiresAuth
  @PutMapping("/{userId}")
  public ResponseEntity<UserDto.Response> updateUser(
      @PathVariable UUID userId,
      @Valid @RequestBody UserDto.Update updateDto) {

    return ResponseEntity.ok(userService.updateUser(userId, updateDto));
  }

  @RequiresAuth
  @PutMapping("{userId}/status")
  public ResponseEntity<StatusDto.StatusResponse> updateUserStatus(
      @Valid @PathVariable UUID userId, @RequestBody StatusDto.StatusRequest statusRequest) {
    return ResponseEntity.ok(userStatusService.updateUserStatus(userId, statusRequest));
  }
}