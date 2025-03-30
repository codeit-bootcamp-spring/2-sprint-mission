package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ApiResponse;
import com.sprint.mission.discodeit.dto.UpdateUserStatusByUserIdParamDto;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/status")
@RequiredArgsConstructor
public class UserStatusController {

    private final UserStatusService userStatusService;

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Void>> updateUserStatus(@RequestBody UpdateUserStatusByUserIdParamDto updateUserStatusByUserIdParamDto) {
        userStatusService.updateByUserId(updateUserStatusByUserIdParamDto);
        return ResponseEntity.ok(ApiResponse.success());
    }
}
