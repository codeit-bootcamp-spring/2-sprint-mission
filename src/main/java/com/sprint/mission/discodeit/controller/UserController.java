package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.UserApi;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController implements UserApi {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Override
    public ResponseEntity<UserDto> create(
        @RequestPart("userCreateRequest") @Valid UserCreateRequest userCreateRequest,
        // required = false 는 값이 안 들어올 수도 있다는 의미
        @RequestPart(value = "profile", required = false) MultipartFile profile
    ) {
        Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
            .flatMap(this::resolveProfileRequest);
        // log
        // {}를 안 적어도 결과는 같지만, 성능상 유리하고, lazy evaluation을 지원한다.
        log.info("사용자 생성 요청: {}", userCreateRequest.username());
        UserDto createdUser = userService.create(userCreateRequest, profileRequest);
        log.info("사용자 생성 성공: {}", createdUser.username());
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(createdUser);
    }

    @PatchMapping(
        path = "{userId}",
        consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    @Override
    public ResponseEntity<UserDto> update(
        @PathVariable("userId") UUID userId,
        @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
        @RequestPart(value = "profile", required = false) MultipartFile profile
    ) {
        Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
            .flatMap(this::resolveProfileRequest);
        // log
        log.info("사용자 수정 요청: {}", userUpdateRequest.newUsername());
        UserDto updatedUser = userService.update(userId, userUpdateRequest, profileRequest);
        log.info("사용자 수정 성공: {}", updatedUser.username());
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(updatedUser);
    }

    @DeleteMapping(path = "{userId}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable("userId") UUID userId) {
        // log
        log.warn("사용자 삭제 요청: {}", userId);
        userService.delete(userId);
        log.info("사용자 삭제 완료: {}", userId);
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }

    @GetMapping
    @Override
    public ResponseEntity<List<UserDto>> findAll() {
        // log
        log.debug("사용자 목록 조회 요청");
        List<UserDto> users = userService.findAll();
        log.info("사용자 목록 조회 성공 및 전체 사용자 수: {}", users.size());
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(users);
    }

    @PatchMapping(path = "{userId}/userStatus")
    @Override
    public ResponseEntity<UserStatusDto> updateUserStatusByUserId(
        @PathVariable("userId") UUID userId,
        @RequestBody UserStatusUpdateRequest request) {
        // log
        log.debug("사용자 상태 변경 요청: {}", userId);
        UserStatusDto updatedUserStatus = userStatusService.updateByUserId(userId, request);
        log.info("사용자 상태 변경 성공: {}", updatedUserStatus.userId());
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(updatedUserStatus);
    }

    private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profileFile) {
        if (profileFile.isEmpty()) {
            log.debug("프로필 파일이 비어있어서 upload 생략");
            return Optional.empty();
        } else {
            try {
                // log
                log.debug("프로필 파일 수신, name: {}", profileFile.getName());
                BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
                    profileFile.getOriginalFilename(),
                    profileFile.getContentType(),
                    profileFile.getBytes()
                );
                return Optional.of(binaryContentCreateRequest);
            } catch (IOException e) {
                log.error("프로필 파일 파싱 중 오류 발생", e);
                throw new DiscodeitException(ErrorCode.FILE_STORAGE_ERROR, null);
            }
        }
    }
}
