package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping
  public ResponseEntity<List<NotificationDto>> getMyNotifications(
      @AuthenticationPrincipal DiscodeitUserDetails principal) {
    List<NotificationDto> notifications = notificationService.findAllByReceiverId(principal.getUserDto().id());
    return ResponseEntity.ok(notifications);
  }

  @DeleteMapping("/{notificationId}")
  @PreAuthorize("@notificationService.isOwner(#notificationId, principal.userDto.id)")
  public ResponseEntity<Void> deleteNotification(
      @PathVariable UUID notificationId,
      @AuthenticationPrincipal DiscodeitUserDetails principal) {
    return ResponseEntity.noContent().build();
  }
}