package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.controller.notification.NotificationDto;
import com.sprint.mission.discodeit.security.CustomUserDetails;
import com.sprint.mission.discodeit.service.basic.BasicNotificationService;
import io.micrometer.core.annotation.Timed;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final BasicNotificationService notificationService;

  @GetMapping
  @Timed(value = "notification.getAll")
  public ResponseEntity<List<NotificationDto>> getNotification(
      @AuthenticationPrincipal CustomUserDetails principal) {
    return ResponseEntity.ok(notificationService.findAll(principal.getUserDto().id()));
  }

  @DeleteMapping("/{notificationId}")
  public ResponseEntity<Void> deleteNotification(
      @PathVariable("notificationId") UUID notificationId,
      @AuthenticationPrincipal CustomUserDetails principal) {
    notificationService.delete(notificationId, principal.getUserDto().id());
    return ResponseEntity.noContent().build();
  }
}
