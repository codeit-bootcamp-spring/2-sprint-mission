package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.dto.IdResponse;
import com.sprint.mission.discodeit.controller.dto.UserStatusUpdateResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserInfoResponse;
import com.sprint.mission.discodeit.service.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.service.dto.user.userstatus.UserStatusUpdateRequest;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    // 사용자 등록
    @RequestMapping(method = RequestMethod.POST, consumes = "multipart/form-data")
    public ResponseEntity<IdResponse> create(
            @RequestPart("userDto") UserCreateRequest userRequest,
            @RequestPart(value = "fileDto", required = false) MultipartFile file
    ) {
        BinaryContentCreateRequest fileData = (file != null) ? new BinaryContentCreateRequest(file) : null;
        User user = userService.create(userRequest, fileData);
        return ResponseEntity.ok(IdResponse.of(true, user.getId()));
    }

    // 사용자 정보 수정
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "multipart/form-data")
    public ResponseEntity<IdResponse> update(
            @PathVariable("id") UUID userId,
            @RequestPart("userUpdateDt0") UserUpdateRequest request,
            @RequestPart(value = "fileDto", required = false) MultipartFile file
    ) {
        BinaryContentCreateRequest fileData = (file != null) ? new BinaryContentCreateRequest(file) : null;
        User user = userService.update(userId, request, fileData);
        return ResponseEntity.ok(IdResponse.of(true, user.getId()));
    }

    // 사용자 삭제
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("id") UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    // 모든 사용자 조회
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserInfoResponse>> findAll() {
        List<UserInfoResponse> response = userService.findAll();
        return ResponseEntity.ok(response);
    }

    // 사용자 온라인 상태 변경
    @RequestMapping(value = "/status/{id}", method = RequestMethod.PUT)
    public ResponseEntity<UserStatusUpdateResponse> updateStatus(
            @PathVariable("id") UUID userId, @RequestBody UserStatusUpdateRequest request
    ) {
        UserStatus userStatus = userStatusService.update(userId, request);
        UserStatusUpdateResponse response = new UserStatusUpdateResponse(true, userStatus.getStatus());
        return ResponseEntity.ok(response);
    }
}
