package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.status.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.status.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.status.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @PostMapping
  @Operation(summary = "읽음 상태 생성")
  @ApiResponse(responseCode = "201", description = "읽음 상태 생성 성공")
  public ResponseEntity<ReadStatusResponse> create(@RequestBody CreateReadStatusRequest request) {
    UUID id = readStatusService.create(request).getId();
    return readStatusService.findById(id)
        .map(response -> ResponseEntity.status(201).body(response))
        .orElse(ResponseEntity.badRequest().build());
  }


  @Operation(summary = "읽음 상태 수정")
  @ApiResponse(
      responseCode = "200",
      description = "읽음 상태 수정 성공",
      content = @Content(mediaType = "*/*") // 이것만 추가!
  )
  @PatchMapping("/{readStatusId}")
  public ResponseEntity<Void> update(
      @PathVariable UUID readStatusId,
      @RequestBody UpdateReadStatusRequest request
  ) {
    if (!readStatusId.equals(request.readStatusId())) {
      return ResponseEntity.badRequest().build();
    }

    readStatusService.update(request);
    return ResponseEntity.ok().build();
  }

  @GetMapping
  @Operation(summary = "사용자의 모든 읽음 상태 조회")
  public ResponseEntity<List<ReadStatusResponse>> findbyUserId(@RequestParam UUID userId) {
    return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
  }
}
