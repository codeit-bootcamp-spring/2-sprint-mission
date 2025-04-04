package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
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
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @PostMapping("")
  @Operation(summary = "Message 읽음 상태 생성", operationId = "create_1")
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ReadStatusRequest.class)
      ),
      required = true
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "Message 읽음 상태가 성공적으로 생성됨",
          content = @Content(
              mediaType = "*/*",
              schema = @Schema(implementation = ReadStatus.class)
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "이미 읽음 상태가 존재함",
          content = @Content(
              mediaType = "*/*",
              examples = @ExampleObject("ReadStatus with userId {userId} and channelId {channelId} already exists")
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Channel 또는 User를 찾을 수 없음",
          content = @Content(
              mediaType = "*/*",
              examples = @ExampleObject("Channel | User with id {channelId | userId} not found")
          )
      )
  })
  public ResponseEntity<ReadStatus> save(
      @RequestBody ReadStatusRequest readStatusRequest
  ) {
    ReadStatus readStatus = readStatusService.save(readStatusRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(readStatus);
  }

  @PatchMapping("/{readStatusId}")
  @Operation(summary = "Message 읽음 상태 수정", operationId = "update_1")
  @Parameters(value = {
      @Parameter(
          name = "readStatusId",
          in = ParameterIn.PATH,
          description = "수정할 읽음 상태 ID",
          required = true,
          schema = @Schema(type = "string", format = "uuid")
      )
  })
  @io.swagger.v3.oas.annotations.parameters.RequestBody(
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ReadStatusUpdateRequest.class)
      ),
      required = true
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Message 읽음 상태가 성공적으로 수정됨",
          content = @Content(
              mediaType = "*/*",
              schema = @Schema(implementation = ReadStatus.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Message 읽음 상태를 찾을 수 없음",
          content = @Content(
              mediaType = "*/*",
              examples = @ExampleObject("ReadStatus with id {readStatusId} not found")
          )
      )
  })
  public ResponseEntity<ReadStatus> update(
      @PathVariable("readStatusId") UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest) {
    ReadStatus readStatus = readStatusService.update(readStatusId, readStatusUpdateRequest);
    return ResponseEntity.status(HttpStatus.OK).body(readStatus);
  }

  @GetMapping("")
  @Operation(summary = "User의 Message 읽음 상태 목록 조회", operationId = "findAllByUserId")
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
          description = "Message 읽음 상태 목록 조회 성공",
          content = @Content(
              mediaType = "*/*",
              array = @ArraySchema(schema = @Schema(implementation = ReadStatus.class))
          )
      )
  })
  public ResponseEntity<List<ReadStatus>> findByUserId(
      @RequestParam("userId") UUID readStatusId
  ) {
    List<ReadStatus> readStatusList = readStatusService.findAllByUserId(
        readStatusId);
    return ResponseEntity.status(HttpStatus.OK).body(readStatusList);
  }
}
