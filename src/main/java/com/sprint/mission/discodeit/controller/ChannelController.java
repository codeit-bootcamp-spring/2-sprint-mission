package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.controller.channel.*;
import com.sprint.mission.discodeit.dto.service.channel.ChannelDTO;
import com.sprint.mission.discodeit.dto.service.channel.FindChannelDTO;
import com.sprint.mission.discodeit.dto.service.channel.PrivateChannelDTO;
import com.sprint.mission.discodeit.dto.service.channel.UpdateChannelDTO;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Channel-Controller", description = "Channel 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels")
public class ChannelController {

  private final ChannelService channelService;
  private final ChannelMapper channelMapper;

  @Operation(summary = "공개 채널 생성",
      description = "ChannelType이 public인 공개 채널을 생성합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "공개 채널 생성 성공"),
          @ApiResponse(responseCode = "400", description = "필드가 올바르게 입력되지 않음 (DTO 유효성 검증 실패)")
      }
  )
  @PostMapping("/public")
  public ResponseEntity<CreatePublicChannelResponseDTO> createPublicChannel(
      @RequestBody @Valid CreatePublicChannelRequestDTO createChannelRequestDTO) {
    ChannelDTO channelDTO =
        channelService.createPublicChannel(channelMapper.toChannelParam(createChannelRequestDTO));
    CreatePublicChannelResponseDTO createdChannel = channelMapper.toChannelResponseDTO(channelDTO);
    return ResponseEntity.ok(createdChannel);
  }

  @Operation(summary = "비공개 채널 생성",
      description = "ChannelType이 private인 비공개 채널을 생성합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "비공개 채널 생성 성공"),
          @ApiResponse(responseCode = "400", description = "필드가 올바르게 입력되지 않음 (DTO 유효성 검증 실패)")
      }
  )
  @PostMapping("/private")
  public ResponseEntity<CreatePrivateChannelResponseDTO> createPrivateChannel(
      @RequestBody @Valid CreatePrivateChannelRequestDTO createPrivateChannelRequestDTO) {
    PrivateChannelDTO privateChannelDTO =
        channelService.createPrivateChannel(
            channelMapper.toPrivateChannelParam(createPrivateChannelRequestDTO));
    CreatePrivateChannelResponseDTO createdChannel = channelMapper.toPrivateChannelResponseDTO(
        privateChannelDTO);
    return ResponseEntity.ok(createdChannel);
  }

  @Operation(summary = "공개 채널 수정",
      description = "공개 채널을 수정합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "공개 채널 수정 성공"),
          @ApiResponse(responseCode = "400", description = "필드가 올바르게 입력되지 않음 (DTO 유효성 검증 실패)"),
          @ApiResponse(responseCode = "403", description = "비밀 채널은 수정이 불가능"),
          @ApiResponse(responseCode = "404", description = "수정하려는 channelId에 해당하는 Channel이 존재하지 않음")
      })
  @PatchMapping("/{channelId}")
  public ResponseEntity<UpdateChannelResponseDTO> updateChannel(
      @PathVariable("channelId") UUID channelId,
      @RequestBody @Valid UpdateChannelRequestDTO updateChannelRequestDTO) {
    UpdateChannelDTO updateChannelDTO =
        channelService.update(channelId,
            channelMapper.toUpdateChannelParam(updateChannelRequestDTO));
    UpdateChannelResponseDTO updatedChannel = channelMapper.toUpdateChannelResponseDTO(
        updateChannelDTO);
    return ResponseEntity.ok(updatedChannel);
  }

  @Operation(summary = "채널 삭제",
      description = "channelId로 채널을 삭제합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "채널 삭제 성공")
      })
  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> deleteChannel(
      @PathVariable("channelId") UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @Operation(summary = "유저가 포함된 채널 조회",
      description = "userId가 포함된 채널들을 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "유저가 포함된 채널 조회 성공")
      })
  @GetMapping
  public ResponseEntity<List<FindChannelDTO>> findAll(@RequestParam("userId") UUID userId) {
    List<FindChannelDTO> channels = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(channels);
  }
}


