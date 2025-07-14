package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.notification.NotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<List<NotificationDto>> getMyNotifications() {
        UUID userId = ((DiscodeitUserDetails) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal()).getUserDto().id();
        return ResponseEntity.ok(notificationService.getNotificationsForUser(userId));
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable UUID notificationId) {
        UUID userId = ((DiscodeitUserDetails) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal()).getUserDto().id();
        notificationService.deleteNotification(userId, notificationId);
        return ResponseEntity.noContent().build();
    }
}

