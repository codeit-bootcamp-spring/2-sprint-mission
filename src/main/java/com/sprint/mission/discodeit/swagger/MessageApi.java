package com.sprint.mission.discodeit.swagger;

import com.sprint.mission.discodeit.dto.controller.message.CreateMessageRequestDTO;
import com.sprint.mission.discodeit.dto.controller.message.CreateMessageResponseDTO;
import com.sprint.mission.discodeit.dto.controller.message.FindMessageResponseDTO;
import com.sprint.mission.discodeit.dto.controller.message.UpdateMessageRequestDTO;
import com.sprint.mission.discodeit.dto.controller.message.UpdateMessageResponseDTO;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Message-Controller", description = "Message 관련 API")
public interface MessageApi {

  @Operation(summary = "메시지 생성",
      description = "userId와 channelId를 가지는 메시지를 생성합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "메시지 생성 성공"),
          @ApiResponse(responseCode = "404", description = "userId 또는 channelId에 해당하는 리소스가 존재하지 않음"),
      })
  ResponseEntity<CreateMessageResponseDTO> createMessage(
      CreateMessageRequestDTO createMessageRequestDTO,
      List<MultipartFile> multipartFileList);

  @Operation(summary = "메시지 수정",
      description = "messageId에 해당하는 메시지를 수정합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "메시지 수정 성공"),
          @ApiResponse(responseCode = "400", description = "필드가 올바르게 입력되지 않음 (DTO 유효성 검증 실패)"),
          @ApiResponse(responseCode = "404", description = "messageId에 해당하는 메시지가 존재하지 않음")
      })
  ResponseEntity<UpdateMessageResponseDTO> updateMessage(UUID id,
      UpdateMessageRequestDTO updateMessageRequestDTO,
      List<MultipartFile> multipartFileList);

  @Operation(summary = "메시지 삭제",
      description = "messageId로 메시지를 삭제합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "메시지 삭제 성공")
      })
  ResponseEntity<Void> deleteMessage(UUID id);

  @Operation(summary = "채널 내 메시지 조회",
      description = "channelId를 가진 메시지들을 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "채널 내 메시지 조회 성공")
      })
  ResponseEntity<PageResponse<FindMessageResponseDTO>> getChannelMessages(UUID id,
      Instant cursor, int limit);

}
