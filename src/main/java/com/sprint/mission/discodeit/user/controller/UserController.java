package com.sprint.mission.discodeit.user.controller;

import com.sprint.mission.discodeit.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.user.dto.UserResult;
import com.sprint.mission.discodeit.user.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.user.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.user.service.UserService;
import com.sprint.mission.discodeit.userstatus.dto.UserStatusResult;
import com.sprint.mission.discodeit.userstatus.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.userstatus.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "사용자 관련 API")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @Operation(
            summary = "사용자 등록",
            description = "프로필 이미지를 포함한 사용자 등록"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 등록 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResult> register(
            @Parameter(description = "User 생성 정보", required = true)
            @Valid @RequestPart UserCreateRequest userCreateRequest,

            @Parameter(description = "사용자 프로필 이미지 (선택)")
            @RequestPart(required = false) MultipartFile profileImage) {

        BinaryContentRequest binaryContentRequest = null;
        if (profileImage != null) {
            binaryContentRequest = BinaryContentRequest.fromMultipartFile(profileImage);
        }
        UserResult user = userService.register(userCreateRequest, binaryContentRequest);

        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "사용자 조회",
            description = "사용자 ID를 이용한 사용자 등록"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResult> getById(
            @Parameter(description = "유저 아이디", required = true)
            @PathVariable UUID userId) {

        UserResult user = userService.getById(userId);

        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "전체 사용자 조회",
            description = "등록된 전체 사용자 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전체 사용자 조회 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
    @GetMapping
    public ResponseEntity<List<UserResult>> getAll() {
        return ResponseEntity.ok(userService.getAllIn());
    }

    @Operation(
            summary = "사용자 정보 수정",
            description = "등록된 사용자 수정"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 수정 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResult> updateUser(
            @Parameter(description = "유저 ID", required = true)
            @PathVariable UUID userId,

            @Parameter(description = "유저 수정 정보", required = true)
            @RequestPart UserUpdateRequest userUpdateRequest,

            @Parameter(description = "사용자 프로필 이미지 (선택)")
            @RequestPart(required = false) MultipartFile profileImage) {

        BinaryContentRequest binaryContentRequest = null;
        if (profileImage != null) {
            binaryContentRequest = BinaryContentRequest.fromMultipartFile(profileImage);
        }
        UserResult updatedUser = userService.update(userId, userUpdateRequest, binaryContentRequest);

        return ResponseEntity.ok(updatedUser);
    }

    @Operation(
            summary = "유저 상태 변경",
            description = "현재 시점으로 유저 상태 업데이트"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 수정 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
    @PatchMapping("/{userId}/userStatus")
    public ResponseEntity<UserStatusResult> updateOnlineStatus(
            @Parameter(description = "유저 ID", required = true)
            @PathVariable UUID userId,

            @Parameter(description = "유저의 마지막 활동 시간", required = true)
            @RequestBody UserStatusUpdateRequest userStatusUpdateRequest) {

        UserStatusResult status = userStatusService.updateByUserId(userId, userStatusUpdateRequest);

        return ResponseEntity.ok(status);
    }

    @Operation(
            summary = "유저 삭제"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "유저 ID", required = true)
            @PathVariable UUID userId) {

        userService.delete(userId);

        return ResponseEntity.noContent()
                .build();
    }
}
