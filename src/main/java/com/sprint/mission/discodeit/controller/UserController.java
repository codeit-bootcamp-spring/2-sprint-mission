package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @Operation(summary = "User 등록", operationId = "create")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User가 성공적으로 생성됨"),
            @ApiResponse(responseCode = "400", description = "같은 email 또는 username을 사용하는 User가 이미 존재함")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> create(
            @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,
            @RequestPart(value = "profile", required = false) MultipartFile profile) {

        Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile)
                .flatMap(this::resolveProfileRequest);
        User createdUser = userService.create(userCreateRequest, profileRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @Operation(summary = "전체 User 목록 조회", operationId = "findAll")
    @ApiResponse(responseCode = "200", description = "User 목록 조회 성공")
    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> users = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @Operation(summary = "User 정보 수정", operationId = "update")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음"),
            @ApiResponse(responseCode = "400", description = "같은 email 또는 username을 사용하는 User가 이미 존재함"),
            @ApiResponse(responseCode = "200", description = "User 정보가 성공적으로 수정됨")
    })
    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> update(
            @Parameter(
                    name = "userId",
                    description = "수정할 User ID",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable UUID userId,
            @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,
            @RequestPart(value = "profile", required = false) MultipartFile profile) {

        Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile).flatMap(this::resolveProfileRequest);
        User updatedUser = userService.update(userId, userUpdateRequest, profileRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @Operation(summary = "User 삭제", operationId = "delete")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User가 성공적으로 삭제됨"),
            @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음")
    })

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(
            @Parameter(
                    name = "userId",
                    description = "삭제할 User ID",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "User 온라인 상태 업데이트", operationId = "updateUserStatusByUserId")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "해당 User의 UserStatus를 찾을 수 없음"),
            @ApiResponse(responseCode = "200", description = "User 온라인 상태가 성공적으로 업데이트됨")
    })
    @PatchMapping("/{userId}/userStatus")
    public ResponseEntity<UserStatus> updateUserStatusByUserId(
            @Parameter(
                    name = "userId",
                    description = "상태를 변경할 User ID",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable UUID userId,
            @RequestBody UserStatusUpdateRequest request) {
        UserStatus userStatus = userStatusService.updateByUserId(userId, request);
        return ResponseEntity.status(HttpStatus.OK).body(userStatus);

    }

    private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profileFile) {
        if (profileFile == null || profileFile.isEmpty()) {
            return Optional.empty();
        }
        try {
            BinaryContentCreateRequest request = new BinaryContentCreateRequest(
                    profileFile.getOriginalFilename(),
                    profileFile.getContentType(),
                    profileFile.getBytes()
            );
            return Optional.of(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
