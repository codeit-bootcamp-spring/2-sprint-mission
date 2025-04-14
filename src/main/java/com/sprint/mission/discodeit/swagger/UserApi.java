package com.sprint.mission.discodeit.swagger;

import com.sprint.mission.discodeit.dto.controller.user.CreateUserRequestDTO;
import com.sprint.mission.discodeit.dto.controller.user.CreateUserResponseDTO;
import com.sprint.mission.discodeit.dto.controller.user.UpdateUserRequestDTO;
import com.sprint.mission.discodeit.dto.controller.user.UpdateUserResponseDTO;
import com.sprint.mission.discodeit.dto.controller.user.UpdateUserStatusResponseDTO;
import com.sprint.mission.discodeit.dto.controller.user.UserResponseDTO;
import com.sprint.mission.discodeit.dto.service.user.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User-Controller", description = "User 관련 API")
public interface UserApi {

  @Operation(summary = "회원가입",
      description = "User를 생성합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "유저 회원가입 성공"),
          @ApiResponse(responseCode = "400", description = "필드가 올바르게 입력되지 않음 (DTO 유효성 검증 실패)"),
          @ApiResponse(responseCode = "404", description = "binaryContentId에 해당하는 BinaryContent를 찾지 못함"),
          @ApiResponse(responseCode = "409", description = "중복된 username 또는 email 입력됨"),
          @ApiResponse(responseCode = "500", description = "프로필 파일 읽기에 실패함"),

      })
  @PostMapping
  ResponseEntity<CreateUserResponseDTO> createUser(CreateUserRequestDTO createUserRequestDTO,
      MultipartFile multipartFile);

  @Operation(summary = "유저 단건 조회",
      description = "userId에 해당하는 유저를 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "유저 단건 조회 성공"),
          @ApiResponse(responseCode = "404", description = "userId에 해당하는 User를 찾지 못함")
      })
  ResponseEntity<UserResponseDTO> getUser(UUID id);

  @Operation(summary = "유저 다건 조회",
      description = "모든 유저를 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "유저 다건 조회 성공")
      })
  ResponseEntity<List<UserDTO>> getUserAll();

  @Operation(summary = "유저 수정",
      description = "userId에 해당하는 유저를 수정합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "유저 수정 성공"),
          @ApiResponse(responseCode = "404", description = "userId에 해당하는 User를 찾지 못함")
      })
  ResponseEntity<UpdateUserResponseDTO> updateUser(UUID id,
      UpdateUserRequestDTO updateUserRequestDTO,
      MultipartFile multipartFile);

  @Operation(summary = "유저상태 수정",
      description = "userId에 해당하는 유저상태를 수정합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "유저상태 수정 성공"),
          @ApiResponse(responseCode = "404", description = "userStatusId에 해당하는 UserStatus를 찾지 못함")
      })
  ResponseEntity<UpdateUserStatusResponseDTO> updateUserStatus(UUID id);

  @Operation(summary = "유저 삭제",
      description = "userId로 유저를 삭제합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "유저 삭제 성공")
      })
  ResponseEntity<Void> deleteUser(@PathVariable("userId") UUID id);
}
