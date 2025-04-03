package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ApiDataResponse;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserStatusController {

  private final UserStatusService userStatusService;

  @PutMapping("/{userId}/status")
  public ResponseEntity<ApiDataResponse<Void>> updateUserStatus(
      @PathVariable UUID userId
  ) {
    userStatusService.updateByUserId(userId);
    return ResponseEntity.ok(ApiDataResponse.success());
  }
}
