package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.NotificationService;
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

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getMyNotifications(
        @AuthenticationPrincipal DiscodeitUserDetails principal
    ) {
        UUID receiverId = principal.getUserDto().id();
        List<NotificationDto> notifications = notificationService.getMyNotifications(receiverId);
        return ResponseEntity.ok(notifications);
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteMyNotification(
        @PathVariable UUID notificationId,
        @AuthenticationPrincipal DiscodeitUserDetails principal
    ) {
        UUID receiverId = principal.getUserDto().id();
        notificationService.deleteMyNotification(notificationId, receiverId);
        return ResponseEntity.noContent().build();
    }
}
