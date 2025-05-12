package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.file.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.status.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @Operation(summary = "사용자 생성")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "사용자 생성 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> createUser(
        @RequestPart(value = "userCreateRequest", required = false) @Valid CreateUserRequest request,
        @RequestPart(value = "profile", required = false) MultipartFile profileFile) {

        log.info("사용자 생성 API 호출 - username: {}, email: {}",
            request != null ? request.username() : "null",
            request != null ? request.email() : "null");

        Optional<CreateBinaryContentRequest> profileOpt = Optional.ofNullable(profileFile)
            .filter(file -> !file.isEmpty())
            .map(file -> {
                try {
                    return new CreateBinaryContentRequest(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getBytes()
                    );
                } catch (IOException e) {
                    log.error("프로필 이미지 변환 실패", e);
                    throw new RuntimeException("프로필 이미지 변환 실패", e);
                }
            });

        UserDto user = userService.createUser(request, profileOpt);
        log.info("사용자 생성 완료 - userId: {}", user.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Operation(
        summary = "사용자 수정",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "사용자 수정 성공",
                content = @Content(mediaType = "*/*")
            )
        }
    )
    @PatchMapping(path = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUser(
        @PathVariable UUID userId,
        @RequestPart(value = "userUpdateRequest", required = false) @Valid UpdateUserRequest request,
        @RequestPart(value = "profile", required = false) MultipartFile profileFile) {

        log.info("사용자 수정 API 호출 - userId: {}", userId);

        UpdateUserRequest realRequest = new UpdateUserRequest(
            userId,
            request.newUsername(),
            request.newPassword(),
            request.newEmail()
        );

        Optional<CreateBinaryContentRequest> profileOpt = Optional.ofNullable(profileFile)
            .filter(file -> !file.isEmpty())
            .map(file -> {
                try {
                    return new CreateBinaryContentRequest(
                        profileFile.getOriginalFilename(),
                        profileFile.getContentType(),
                        profileFile.getBytes()
                    );
                } catch (IOException e) {
                    log.error("프로필 이미지 변환 실패", e);
                    throw new RuntimeException("프로필 이미지 변환 실패", e);
                }
            });

        userService.updateUser(realRequest, profileOpt);
        log.info("사용자 수정 완료 - userId: {}", userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "사용자 삭제")
    @ApiResponse(responseCode = "204", description = "사용자 삭제 성공")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        log.info("사용자 삭제 API 호출 - userId: {}", userId);
        userService.deleteUser(userId);
        log.info("사용자 삭제 완료 - userId: {}", userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "전체 사용자 조회")
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(
        summary = "온라인 상태 변경",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "상태 변경 성공",
                content = @Content(mediaType = "*/*")
            )
        }
    )
    @PatchMapping("/{userId}/userStatus")
    public ResponseEntity<Void> updateOnlineStatus(
        @PathVariable UUID userId,
        @RequestBody @Valid UpdateUserStatusRequest request
    ) {
        log.info("사용자 상태 변경 API 호출 - userId: {}", userId);
        userStatusService.update(new UpdateUserStatusRequest(userId, request.newLastActiveAt()));
        log.info("사용자 상태 변경 완료 - userId: {}", userId);
        return ResponseEntity.ok().build();
    }
}
