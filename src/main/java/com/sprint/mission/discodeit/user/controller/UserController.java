package com.sprint.mission.discodeit.user.controller;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.user.dto.UserResult;
import com.sprint.mission.discodeit.user.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.user.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.user.service.UserService;
import com.sprint.mission.discodeit.userstatus.dto.UserStatusResult;
import com.sprint.mission.discodeit.userstatus.service.UserStatusService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResult> register(@Valid @RequestPart UserCreateRequest userCreateRequest, @RequestPart(required = false) MultipartFile profileImage) {
        log.info("사용자 생성 요청: username={}, email={}", userCreateRequest.username(), userCreateRequest.email());
        BinaryContentRequest binaryContentRequest = getBinaryContentRequest(profileImage);
        UserResult user = userService.register(userCreateRequest, binaryContentRequest);
        log.info("사용자 생성 성공: userId={}", user.id());

        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResult> getById(@PathVariable UUID userId) {
        log.debug("사용자 조회 요청: userId={}", userId);
        UserResult user = userService.getById(userId);
        log.info("사용자 조회 성공: userId={}", userId);

        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResult>> getAll() {
        log.debug("전체 사용자 목록 조회 요청");
        List<UserResult> users = userService.getAllIn();
        log.info("전체 사용자 목록 조회 성공: count={}", users.size());

        return ResponseEntity.ok(users);
    }

    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResult> updateUser(@PathVariable UUID userId, @Valid @RequestPart UserUpdateRequest userUpdateRequest, @RequestPart(required = false) MultipartFile profileImage) {
        log.info("사용자 수정 요청: userId={}, newUsername={}, newEmail={}", userId, userUpdateRequest.newUsername(), userUpdateRequest.newEmail());
        BinaryContentRequest binaryContentRequest = getBinaryContentRequest(profileImage);
        UserResult updatedUser = userService.update(userId, userUpdateRequest, binaryContentRequest);
        log.info("사용자 수정 성공: userId={}", userId);

        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{userId}/userStatus")
    public ResponseEntity<UserStatusResult> updateOnlineStatus(@PathVariable UUID userId) {
        log.info("사용자 온라인 상태 갱신 요청: userId={}", userId);
        UserStatusResult status = userStatusService.updateByUserId(userId, Instant.now());
        log.info("사용자 온라인 상태 갱신 성공: userId={}", userId);

        return ResponseEntity.ok(status);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable UUID userId) {
        log.warn("사용자 삭제 요청: userId={}", userId);
        userService.delete(userId);
        log.info("사용자 삭제 성공: userId={}", userId);

        return ResponseEntity.noContent().build();
    }

    private BinaryContentRequest getBinaryContentRequest(MultipartFile profileImage) {
        if (profileImage == null) {
            log.debug("프로필 이미지가 첨부되지 않음");
            return null;
        }
        log.debug("프로필 이미지 업로드 요청: filename={}, size={}", profileImage.getOriginalFilename(), profileImage.getSize());

        return BinaryContentRequest.fromMultipartFile(profileImage);
    }

}
