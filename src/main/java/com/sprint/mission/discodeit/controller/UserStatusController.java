package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.userstatus.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
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
        @PathVariable("userId") UUID userId,
        @RequestBody UpdateUserStatusRequest request
    ) {

        try {
            UserStatusDto response = userStatusService.updateByUserId(userId, request);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("[ERROR] UserStatus with userId " + userId + " not found");
        }
    }
}
