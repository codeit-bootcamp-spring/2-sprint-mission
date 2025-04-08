package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readstatuses")
@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @Operation(summary = "Message 읽음 상태 생성")
  @PostMapping()
  public ResponseEntity<ReadStatus> create(@RequestBody ReadStatusCreateRequest request) {
    System.out.println("읽음 상태 최초 생성 API 실행");
    ReadStatus readStatus = readStatusService.create(request);
    System.out.println("readStatus :" + readStatus);
    return ResponseEntity.ok(readStatus);
  }

  @Operation(summary = "Message 읽음 상태 수정")
  @PatchMapping("/{readStatusId}")
  public ResponseEntity<ReadStatus> update(
      @PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest request) {
    System.out.println("수신 정보 업데이트 API 실행");
    ReadStatus readStatus = readStatusService.update(readStatusId, request);
    return ResponseEntity.ok(readStatus);
  }

  @Operation(summary = "User의 Message 읽음 상태 목록 조회")
  @GetMapping()
  public ResponseEntity<List<ReadStatus>> findAll(@RequestParam UUID userId) {
    System.out.println("수신 정보 전체 조회 API 실행");
    List<ReadStatus> readStatusList = readStatusService.findAllByUserId(userId);
    System.out.println("참여 톡방 읽음 상태 리스트 : " + readStatusList);
    return ResponseEntity.ok(readStatusList);
  }

}
