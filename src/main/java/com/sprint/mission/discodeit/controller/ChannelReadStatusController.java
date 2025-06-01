package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "채널 기반 읽음 상태 API", description = "채널 내 읽음 상태 관련 기능을 제공합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/channels/{channelId}/read-status")
public class ChannelReadStatusController {

  private final ReadStatusService readStatusService;

  @Operation(summary = "읽음 상태 생성", description = "특정 채널에서 새로운 읽음 상태를 생성합니다.")
  @PostMapping("/create")
  public ResponseEntity<ReadStatus> createReadStatus(
      @PathVariable UUID channelId,
      @RequestBody ReadStatusCreateRequest request
  ) {
    ReadStatus created = readStatusService.create(request);
    return ResponseEntity.ok(created);
  }

  @Operation(summary = "사용자의 읽음 상태 목록 조회", description = "특정 사용자의 모든 읽음 상태를 조회합니다.")
  @GetMapping
  public ResponseEntity<List<ReadStatus>> getUserReadStatuses(@RequestParam UUID userId) {
    List<ReadStatus> list = readStatusService.findAllByUserId(userId);
    return ResponseEntity.ok(list);
  }
}
