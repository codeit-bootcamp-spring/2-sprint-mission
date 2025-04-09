package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.userstatus.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserStatusController {

    private final UserStatusService userStatusService;

    @PatchMapping("/api/users/{userId}/userStatus")
    public ResponseEntity<?> updateUserStatusByUserId(
        @PathVariable UUID userId,
        @RequestBody UpdateUserStatusRequest request
    ) {

        try {
            UUID userStatusId = userStatusService.updateByUserId(userId, request);
            UserStatus userStatus = userStatusService.findByUserStatusId(userStatusId);

            return ResponseEntity.ok(userStatus);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("UserStatus with userId " + userId + " not found");
        }
    }
}
