package com.sprint.mission.discodeit.swagger;

import com.sprint.mission.discodeit.dto.controller.channel.CreatePrivateChannelRequestDTO;
import com.sprint.mission.discodeit.dto.controller.channel.CreatePrivateChannelResponseDTO;
import com.sprint.mission.discodeit.dto.controller.channel.CreatePublicChannelRequestDTO;
import com.sprint.mission.discodeit.dto.controller.channel.CreatePublicChannelResponseDTO;
import com.sprint.mission.discodeit.dto.controller.channel.UpdateChannelRequestDTO;
import com.sprint.mission.discodeit.dto.controller.channel.UpdateChannelResponseDTO;
import com.sprint.mission.discodeit.dto.service.channel.FindChannelDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;


@Tag(name = "Channel-Controller", description = "Channel 관련 API")
public interface ChannelApi {

  @Operation(summary = "공개 채널 생성",
      description = "ChannelType이 public인 공개 채널을 생성합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "공개 채널 생성 성공"),
          @ApiResponse(responseCode = "400", description = "필드가 올바르게 입력되지 않음 (DTO 유효성 검증 실패)")
      }
  )
  ResponseEntity<CreatePublicChannelResponseDTO> createPublicChannel(
      CreatePublicChannelRequestDTO createChannelRequestDTO);

  @Operation(summary = "비공개 채널 생성",
      description = "ChannelType이 private인 비공개 채널을 생성합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "비공개 채널 생성 성공"),
          @ApiResponse(responseCode = "400", description = "필드가 올바르게 입력되지 않음 (DTO 유효성 검증 실패)")
      }
  )
  ResponseEntity<CreatePrivateChannelResponseDTO> createPrivateChannel(
      CreatePrivateChannelRequestDTO createPrivateChannelRequestDTO);

  @Operation(summary = "공개 채널 수정",
      description = "공개 채널을 수정합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "공개 채널 수정 성공"),
          @ApiResponse(responseCode = "400", description = "필드가 올바르게 입력되지 않음 (DTO 유효성 검증 실패)"),
          @ApiResponse(responseCode = "403", description = "비밀 채널은 수정이 불가능"),
          @ApiResponse(responseCode = "404", description = "수정하려는 channelId에 해당하는 Channel이 존재하지 않음")
      })
  ResponseEntity<UpdateChannelResponseDTO> updateChannel(UUID channelId,
      UpdateChannelRequestDTO updateChannelRequestDTO);

  @Operation(summary = "채널 삭제",
      description = "channelId로 채널을 삭제합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "채널 삭제 성공")
      })
  ResponseEntity<Void> deleteChannel(UUID channelId);

  @Operation(summary = "유저가 포함된 채널 조회",
      description = "userId가 포함된 채널들을 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "유저가 포함된 채널 조회 성공")
      })
  ResponseEntity<List<FindChannelDTO>> findAll(UUID userId);

}
