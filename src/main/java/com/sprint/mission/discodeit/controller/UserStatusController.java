package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserStatusController {

  private final UserStatusService userStatusService;

  @PatchMapping("/{userId}/userStatus")
  public ResponseEntity<Void> updateUserStatus(
      @PathVariable UUID userId,
      @RequestBody UserStatusUpdateRequest updateRequest) {

    userStatusService.updateByUserId(userId, updateRequest);

    return new ResponseEntity<>(HttpStatus.OK);
  }
}