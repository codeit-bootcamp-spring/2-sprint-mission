package com.sprint.mission.discodeit.domain.notification.controller;

import com.sprint.mission.discodeit.domain.notification.dto.NotificationResult;
import com.sprint.mission.discodeit.domain.notification.service.NotificationService;
import com.sprint.mission.discodeit.domain.user.dto.UserResult;
import com.sprint.mission.discodeit.security.userDetails.CustomUserDetails;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping
  public ResponseEntity<List<NotificationResult>> getAllByUserId(
      @AuthenticationPrincipal CustomUserDetails principal
  ) {
    UUID userId = principal.getUserResult().id();
    return ResponseEntity.ok(notificationService.getAllByUserId(userId));
  }

  @DeleteMapping("/{notificationId}")
  public ResponseEntity<Void> delete(@PathVariable(name = "notificationId") UUID notificationId) {
    notificationService.delete(notificationId);
    return ResponseEntity.noContent().build();
  }

}
