package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentCreateDto;
import com.sprint.mission.discodeit.service.dto.userdto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "User", description = "User API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "User 등록")
    @ApiResponse(responseCode = "200", description = "user가 성공적으로 생성됨")
    @ApiResponse(responseCode = "400", description = "같은 email 또는 username을 사용하는 User가 이미 존재함", content = @Content(examples = @ExampleObject(value = "User already exists or Email already exists")))
    public ResponseEntity<User> createUser(
            @RequestPart("userInfo") UserCreateDto userCreateRequest,
            @RequestPart(value = "file", required = false) @Parameter(description = "User 프로필 이미지") MultipartFile file
    ) {
        Optional<BinaryContentCreateDto> contentCreate = Optional.empty();
        if (file != null && !file.isEmpty()) {
            try {
                contentCreate = Optional.of(new BinaryContentCreateDto(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getBytes()
                ));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        User user = userService.create(userCreateRequest, contentCreate);
        return ResponseEntity.ok(user);
    }


    @PatchMapping("/{userId}/userStatus")
    @Operation(summary = "User 온라인 상태 업데이트")
    @ApiResponse(responseCode = "404", description = "해당 User의 UserStatus를 찾을 수 없음", content = @Content(examples = @ExampleObject(value = "User does not exist")))
    @ApiResponse(responseCode = "200", description = "User 온라인 상태가 성공적으로 업데이트됨")
    public ResponseEntity<UserStatus> updateUserStatusByUserId(
            @PathVariable @Parameter(description = "상태를 변경할 User ID") UUID userId
    ) {
        UserStatus updateUserStatusResponse = userStatusService.updateByUserId(userId);
        return ResponseEntity.ok(updateUserStatusResponse);
    }


    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "User 정보 수정")
    @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음", content = @Content(examples = @ExampleObject(value = "User does not exist")))
    @ApiResponse(responseCode = "400", description = "같은 email 또는 username을 사용하는 User가 이미 존재함", content = @Content(examples = @ExampleObject(value = "User or Email already exists")))
    @ApiResponse(responseCode = "200", description = "User 정보가 성공적으로 수정됨")
    public ResponseEntity<User> updateUser(
            @PathVariable @Parameter(description = "수정 할 User ID") UUID userId,
            @RequestPart("newUserInfo") UserUpdateDto userUpdateRequest,
            @RequestPart(value = "file", required = false) @Parameter(description = "수정 할 User 프로필 이미지") MultipartFile file
    ) {
        Optional<BinaryContentCreateDto> contentCreate = Optional.empty();
        if (file != null && !file.isEmpty()) {
            try {
                contentCreate = Optional.of(new BinaryContentCreateDto(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file.getBytes()
                ));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        User user = userService.update(userId, userUpdateRequest, contentCreate);
        return ResponseEntity.ok(user);
    }


    @DeleteMapping("/{userId}")
    @Operation(summary = "User 삭제")
    @ApiResponse(responseCode = "204", description = "User가 성공적으로 삭제됨")
    @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음", content = @Content(examples = @ExampleObject(value = "User does not exist")))
    public ResponseEntity<Void> deleteUser(
            @PathVariable @Parameter(description = "삭제 할 User ID") UUID userId
    ) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/find")
    public ResponseEntity<UserFindResponseDto> findUser(
            @RequestBody UserFindRequestDto userFindRequest
    ) {
        UserFindResponseDto userFindResponse = userService.find(userFindRequest);
        return ResponseEntity.ok(userFindResponse);
    }


    @GetMapping
    @Operation(summary = "전체 User 목록 조회")
    @ApiResponse(responseCode = "200", description = "User 목록 조회 성공")
    public ResponseEntity<List<UserFindAllResponseDto>> findAllUser() {
        List<UserFindAllResponseDto> userFindAllResponse = userService.findAllUser();
        return ResponseEntity.ok(userFindAllResponse);
    }

}
