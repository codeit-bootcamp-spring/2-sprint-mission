package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.service.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.user.UserDto;
import com.sprint.mission.discodeit.service.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.service.dto.user.userstatus.UserStatusUpdateDto;
import com.sprint.mission.discodeit.service.dto.user.userstatus.UserStatusUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User", description = "User API")
public interface UserApi {

  // 사용자 등록
  @Operation(summary = "User 등록")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "User가 성공적으로 생성됨",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = UserDto.class))),
      @ApiResponse(responseCode = "400", description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
          content = @Content(mediaType = "*/*", examples = {
              @ExampleObject(value = "{email}은 중복된 email")
          }))
  })
  ResponseEntity<UserDto> create(
      @Parameter(description = "User 생성 정보") @Valid UserCreateRequest userRequest,
      @Parameter(description = "User 프로필 이미지") MultipartFile profile
  );

  // 사용자 정보 수정
  @Operation(summary = "User 정보 수정")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User가 성공적으로 수정됨",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = UserDto.class))),
      @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음",
          content = @Content(mediaType = "*/*", examples = {
              @ExampleObject(value = "{userId}에 해당하는 User를 찾을 수 없음")
          })),
      @ApiResponse(responseCode = "400", description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
          content = @Content(mediaType = "*/*", examples = {
              @ExampleObject(value = "{newEmail}은 중복된 email")
          }))
  })
  ResponseEntity<UserDto> update(
      @Parameter(description = "수정할 User ID") UUID userId,
      @Parameter(description = "수정할 User 정보") @Valid UserUpdateRequest request,
      @Parameter(description = "수정할 User 프로필 이미지") MultipartFile profile
  );

  // 사용자 삭제
  @Operation(summary = "User 삭제")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "User가 성공적으로 삭제됨"),
      @ApiResponse(responseCode = "404", description = "User를 찾을 수 없음",
          content = @Content(mediaType = "*/*", examples = {
              @ExampleObject(value = "{userId}에 해당하는 User를 찾을 수 없음")
          }))
  })
  ResponseEntity<Void> delete(
      @Parameter(description = "삭제할 User ID") UUID userId);

  // 모든 사용자 조회
  @Operation(summary = "전체 User 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User 목록 조회 성공",
          content = @Content(mediaType = "*/*",
              array = @ArraySchema(schema = @Schema(implementation = UserDto.class))))
  })
  ResponseEntity<List<UserDto>> findAll();

  // 사용자 온라인 상태 변경
  @Operation(summary = "User 온라인 상태 업데이트")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User 온라인 상태가 성공적으로 업데이트됨",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = UserStatusUpdateDto.class))),
      @ApiResponse(responseCode = "404", description = "해당 User의 UserStatus를 찾을 수 없음",
          content = @Content(mediaType = "*/*", examples = {
              @ExampleObject(value = "{userId}에 해당하는 UserStatus를 찾을 수 없음")
          }))
  })
  ResponseEntity<UserStatusUpdateDto> updateStatus(
      @Parameter(description = "상태를 변경할 User ID") UUID userId,
      @RequestBody UserStatusUpdateRequest request
  );
}
