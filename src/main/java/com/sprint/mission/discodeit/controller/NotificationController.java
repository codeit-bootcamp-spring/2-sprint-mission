package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.NotificationApi;
import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.service.NotificationService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notifications")
public class NotificationController implements NotificationApi {

    private final NotificationService notificationService;
    private final UserService userService;

    @Override
    @GetMapping
    public ResponseEntity<List<NotificationDto>> findNotifications(Authentication authentication) {
            String receiverName = authentication.getName();
            UUID receiverId = userService.transId(receiverName);

        return ResponseEntity.ok(notificationService.getNotifications(receiverId));
    }

    @Override
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotifications(@PathVariable UUID notificationId, Authentication authentication) {
        String receiverName = authentication.getName();
        UUID receiverId = userService.transId(receiverName);

        notificationService.deleteNotification(notificationId,receiverId);

        return ResponseEntity.noContent().build();
    }
}
