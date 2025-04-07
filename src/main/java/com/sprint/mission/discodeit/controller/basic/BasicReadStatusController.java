package com.sprint.mission.discodeit.controller.basic;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.basic.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class BasicReadStatusController {

  private final ReadStatusService readStatusService;
  Logger logger = LoggerFactory.getLogger(BasicReadStatusController.class);

  @Operation(summary = "Message 읽음 상태 생성")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음"),
      @ApiResponse(responseCode = "400", description = "이미 읽음 상태가 존재함"),
      @ApiResponse(responseCode = "201", description = "Message 읽음 상태가 성공적으로 생성됨")
  })
  @PostMapping
  public ResponseEntity<ReadStatus> create(
      @Parameter(description = "Message 읽음 상태 생성 요청 정보")
      @RequestBody ReadStatusCreateRequest request) {
    ReadStatus response = readStatusService.create(request);
    logger.info("Succesfully created readStatus: {}", response);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "Message 읽음 상태 수정")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Message 읽음 상태가 성공적으로 수정됨"),
      @ApiResponse(responseCode = "404", description = "Message 읽음 상태를 찾을 수 없음")
  })
  @PatchMapping("/{readStatusId}")
  public ResponseEntity<ReadStatus> update(
      @Parameter(description = "수정할 읽음 상태 ID")
      @PathVariable UUID readStatusId,
      @Parameter(description = "읽음 상태 수정 요청 정보")
      @RequestBody ReadStatusUpdateRequest request) {
    ReadStatus response = readStatusService.update(readStatusId, request);
    logger.info("Succesfully updated readStatus: {}", response);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "User의 Message 읽음 상태 목록 조회")
  @ApiResponse(responseCode = "200", description = "Message 읽음 상태 목록 조회 성공")
  @GetMapping
  public ResponseEntity<List<ReadStatus>> findAllByUserId(
      @Parameter(name = "userId", description = "조회할 User ID")
      @RequestParam UUID userId) {
    List<ReadStatus> response = readStatusService.findAllByUserId(userId);
    logger.info("Succesfully findAllByUserId: {}", response);
    return ResponseEntity.ok(response);
  }
}
