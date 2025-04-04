package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.SaveChannelResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
@Tag(name = "Channel", description = "Channel API")
public class ChannelController {

  private final ChannelService channelService;

  @PostMapping("/public")
  @Operation(summary = "Public Channel 생성", operationId = "create_3")
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = PublicChannelCreateRequest.class)
      ),
      required = true
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "Public Channel이 성공적으로 생성됨",
          content = @Content(
              mediaType = "*/*",
              schema = @Schema(implementation = Channel.class)
          )
      )
  })
  public ResponseEntity<ApiDataResponse<SaveChannelResponseDto>> createPublic(
      @RequestBody PublicChannelCreateRequest publicChannelCreateRequest
  ) {
    SaveChannelResponseDto saveChannelResponseDto = channelService.createPublicChannel(
        publicChannelCreateRequest);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiDataResponse.success(saveChannelResponseDto));
  }

  @PostMapping("/private")
  @Operation(summary = "Private Channel 생성", operationId = "create_4")
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = PrivateChannelCreateRequest.class)
      ),
      required = true
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "Private Channel이 성공적으로 생성됨",
          content = @Content(
              schema = @Schema(implementation = Channel.class)
          )
      )
  })
  public ResponseEntity<ApiDataResponse<SaveChannelResponseDto>> createPrivate(
      @RequestBody PrivateChannelCreateRequest privateChannelCreateRequest
  ) {
    SaveChannelResponseDto saveChannelResponseDto = channelService.createPrivateChannel(
        privateChannelCreateRequest);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiDataResponse.success(saveChannelResponseDto));
  }

  @PatchMapping("/{channelId}")
  @Operation(summary = "Channel 정보 수정", operationId = "update_3")
  @Parameters(
      @Parameter(
          name = "channelId",
          in = ParameterIn.PATH,
          description = "수정할 Channel ID",
          required = true,
          schema = @Schema(type = "string", format = "uuid")
      )
  )
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = PublicChannelUpdateRequest.class)
      )
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Channel 정보가 성공적으로 수정됨",
          content = @Content(
              mediaType = "*/*",
              schema = @Schema(implementation = Channel.class)
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Private Channel은 수정할 수 없음",
          content = @Content(
              mediaType = "*/*",
              examples = @ExampleObject(value = "Private channel cannot be updated")
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Channel을 찾을 수 없음",
          content = @Content(
              mediaType = "*/*",
              examples = @ExampleObject(value = "Channel with id {channelId} not found")
          )
      )
  })
  public ResponseEntity<ApiDataResponse<Void>> update(
      @PathVariable(name = "channelId") UUID channelId,
      @RequestBody PublicChannelUpdateRequest publicChannelUpdateRequest
  ) {
    channelService.updateChannel(channelId, publicChannelUpdateRequest);
    return ResponseEntity.status(HttpStatus.OK).body(ApiDataResponse.success());
  }

  @DeleteMapping("/{channelId}")
  @Operation(summary = "Channel 삭제", operationId = "delete_2")
  @Parameters(value = {
      @Parameter(
          name = "channelId",
          in = ParameterIn.PATH,
          description = "삭제할 Channel ID",
          required = true,
          schema = @Schema(type = "string", format = "uuid")
      )
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "Channel이 성공적으로 삭제됨"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Channel을 찾을 수 없음",
          content = @Content(
              mediaType = "*/*",
              examples = @ExampleObject(value = "Channel with id {channelId} not found")
          )
      )
  })
  public ResponseEntity<ApiDataResponse<Void>> delete(
      @PathVariable("channelId") UUID channelId
  ) {
    channelService.deleteChannel(channelId);
    return ResponseEntity.status(HttpStatus.OK).body(ApiDataResponse.success());
  }

  @GetMapping("")
  @Operation(summary = "User가 참여 중인 Channel 목록 조회", operationId = "findAll_1")
  @Parameters(value = {
      @Parameter(
          name = "userId",
          in = ParameterIn.QUERY,
          description = "조회할 User ID",
          required = true,
          schema = @Schema(type = "string", format = "uuid")
      )
  })
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Channel 목록 조회 성공",
          content = @Content(
              mediaType = "*/*",
              array = @ArraySchema(schema = @Schema(implementation = ChannelDto.class))
          )
      )
  })
  public ResponseEntity<ApiDataResponse<List<ChannelDto>>> findMyChannel(
      @RequestParam("userId") UUID userId
  ) {
    List<ChannelDto> findMyChannelList = channelService.findAllByUserId(userId);
    return ResponseEntity.status(HttpStatus.OK).body(ApiDataResponse.success(findMyChannelList));
  }
}
