package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "사용자 관리 API")
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @Operation(summary = "사용자 생성", description = "프로필 이미지를 포함한 사용자 계정 생성")
    @ApiResponse(
            responseCode = "201",
            description = "사용자 생성 성공",
            content = @Content(schema = @Schema(implementation = User.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "중복 사용자 존재",
            content = @Content(examples = @ExampleObject(value = "User with email {email} already exists"))
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> create(
            @Parameter(description = "JSON 형식 사용자 데이터", required = true)
            @RequestPart("userCreateRequest") UserCreateRequest userCreateRequest,

            @Parameter(description = "프로필 이미지 파일 (선택)")
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) {
        Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile).flatMap(this::resolveProfileRequest);
        User createdUser = userService.create(userCreateRequest, profileRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @Operation(summary = "사용자 정보 수정", description = "프로필 이미지 포함 사용자 정보 업데이트")
    @ApiResponse(
            responseCode = "200",
            description = "수정 성공",
            content = @Content(schema = @Schema(implementation = User.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "사용자 없음",
            content = @Content(examples = @ExampleObject(value = "사용자를 찾을 수 없습니다."))
    )
    @ApiResponse(
            responseCode = "400",
            description = "중복 데이터 존재",
            content = @Content(examples = @ExampleObject(value = "중복되는 이메일 입니다."))
    )
    @PatchMapping(value = "/{userId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<User> update(
            @Parameter(description = "사용자 UUID", required = true)
            @PathVariable("userId") UUID userId,

            @Parameter(description = "수정 데이터", required = true)
            @RequestPart("userUpdateRequest") UserUpdateRequest userUpdateRequest,

            @Parameter(description = "새 프로필 이미지")
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) {
        Optional<BinaryContentCreateRequest> profileRequest = Optional.ofNullable(profile).flatMap(this::resolveProfileRequest);
        User updatedUser = userService.update(userId, userUpdateRequest, profileRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "사용자 삭제")
    @ApiResponse(responseCode = "204", description = "삭제 성공")
    @ApiResponse(
            responseCode = "404",
            description = "사용자 없음",
            content = @Content(examples = @ExampleObject(value = "사용자를 찾을 수 없습니다."))
    )
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "삭제할 사용자 UUID", required = true)
            @PathVariable("userId") UUID userId
    ) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "전체 사용자 조회")
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(schema = @Schema(type = "array", implementation = UserDto.class))
    )
    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        List<UserDto> users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "사용자 상태 업데이트")
    @ApiResponse(
            responseCode = "200",
            description = "상태 업데이트 성공",
            content = @Content(schema = @Schema(implementation = UserStatus.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "상태 정보 없음",
            content = @Content(examples = @ExampleObject(value = "UserStatus with userId {userId} not found"))
    )
    @PatchMapping("/{userId}/status")
    public ResponseEntity<UserStatus> updateUserStatus(
            @Parameter(description = "대상 사용자 UUID", required = true)
            @PathVariable("userId") UUID userId,

            @RequestBody UserStatusUpdateRequest request
    ) {
        UserStatus updatedUserStatus = userStatusService.updateByUserId(userId, request);
        return ResponseEntity.ok(updatedUserStatus);
    }

    private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profileFile) {
        if (profileFile.isEmpty()) {
            return Optional.empty();
        } else {
            try {
                BinaryContentCreateRequest binaryContentCreateRequest = new BinaryContentCreateRequest(
                        profileFile.getOriginalFilename(),
                        profileFile.getContentType(),
                        profileFile.getBytes()
                );
                return Optional.of(binaryContentCreateRequest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
