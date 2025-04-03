package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ApiDataResponse;
import com.sprint.mission.discodeit.dto.FindUserDto;
import com.sprint.mission.discodeit.dto.SaveBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.SaveUserRequestDto;
import com.sprint.mission.discodeit.dto.UpdateUserRequestDto;
import com.sprint.mission.discodeit.dto.UpdateUserStatusRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User API")
public class UserController {

  private final UserService userService;
  private final UserStatusService userStatusService;

  @PostMapping(
      path = "",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  @Operation(summary = "User 등록", operationId = "create")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "User가 성공적으로 생성됨",
          content = @Content(
              mediaType = "*/*",
              schema = @Schema(implementation = ApiResponse.class)
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "같은 email 또는 username을 사용하는 User가 이미 존재함",
          content = @Content(
              mediaType = "*/*",
              schema = @Schema(implementation = ApiResponse.class)
          )
      )
  })
  public ResponseEntity<ApiDataResponse<Void>> create(
      @RequestPart("userCreateRequest") SaveUserRequestDto saveUserRequestDto,
      @RequestPart(value = "profile", required = false) MultipartFile file
  ) throws IOException {
    System.out.println(saveUserRequestDto);
    userService.save(saveUserRequestDto, SaveBinaryContentRequestDto.nullableFrom(file));
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiDataResponse.success());
  }

  @PutMapping(
      path = "/{userId}",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE
  )
  @Operation(summary = "User 정보 수정", operationId = "update")
  @Parameters(value = {
      @Parameter(name = "userId",
          in = ParameterIn.PATH,
          description = "수정 User ID",
          required = true,
          schema = @Schema(type = "string", format = "uuid")
      )
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "User 정보가 성공적으로 수정됨",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = User.class))
      ),
      @ApiResponse(
          responseCode = "400",
          description = "같은 email 또는 username을 사용하는 User가 이미 존재함",
          content = @Content(
              mediaType = "*.*",
              schema = @Schema(implementation = ApiResponse.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "*/*",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ApiResponse.class)
          )
      )
  })
  public ResponseEntity<ApiDataResponse<Void>> update(
      @PathVariable("userId") UUID userId,
      @RequestPart("userUpdateRequest") UpdateUserRequestDto updateUserRequestDto,
      @RequestPart(value = "profile", required = false) MultipartFile file
  ) throws IOException {
    userService.update(userId, updateUserRequestDto,
        SaveBinaryContentRequestDto.nullableFrom(file));
    return ResponseEntity.ok(ApiDataResponse.success());
  }


  @DeleteMapping("/{userId}")
  @Operation(summary = "User 삭제", operationId = "delete")
  @Parameters(value = {
      @Parameter(
          name = "userId",
          in = ParameterIn.PATH,
          description = "삭제할 UserId",
          required = true,
          schema = @Schema(type = "string", format = "uuid")
      )
  })
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "User가 성공적으로 삭제됨"),
      @ApiResponse(
          responseCode = "404",
          description = "User를 찾을 수 없음",
          content = @Content(
              mediaType = "*/*",
              examples = @ExampleObject(value = "User with id {id} not found")
          )
      )
  })
  public ResponseEntity<ApiDataResponse<Void>> delete(
      @PathVariable("userId") UUID userId
  ) {
    userService.delete(userId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiDataResponse.success());
  }

  @GetMapping("/find")
  @Operation(summary = "전체 User 목록 조회", operationId = "findAll")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "User 목록 조회 성공",
          content = @Content(
              mediaType = "*/*",
              array = @ArraySchema(
                  schema = @Schema(implementation = FindUserDto.class)
              )
          )
      )
  })
  public ResponseEntity<ApiDataResponse<List<FindUserDto>>> findAll() {
    List<FindUserDto> findUserDtoList = userService.findAllUser();
    return ResponseEntity.ok(ApiDataResponse.success(findUserDtoList));
  }

  @PutMapping("/{userId}/status")
  @Operation(summary = "User 온라인 상태 업데이트", operationId = "updateUserStatusByUserId")
  @Parameters(
      @Parameter(
          name = "userId",
          in = ParameterIn.PATH,
          description = "상태를 변경할 User ID",
          required = true,
          schema = @Schema(type = "String", format = "uuid")
      )
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "404",
          description = "해당 User의 UserStatus를 찾을 수 없음",
          content = @Content(
              mediaType = "*/*",
              examples = @ExampleObject(value = "UserStatus with userId {userId} not found")
          )
      ),
      @ApiResponse(
          responseCode = "200",
          description = "User 온라인 상태가 성공적으로 업데이트됨",
          content = @Content(
              mediaType = "*/*",
              schema = @Schema(implementation = UserStatus.class)
          )
      )
  })
  public ResponseEntity<ApiDataResponse<Void>> updateUserStatusByUserId(
      @PathVariable("userId") UUID userId,
      @RequestBody() UpdateUserStatusRequestDto dto
  ) {
    System.out.println(dto.loginTime());
    userStatusService.updateByUserId(userId, dto);
    return ResponseEntity.ok(ApiDataResponse.success());
  }
}
