package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.MessageReadApi;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.dto.service.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.service.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.service.readstatus.ReadStatusUpdateRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readStatuses")
public class MessageReadController implements MessageReadApi {

  private final ReadStatusService readStatusService;

  // 특정 채널의 메시지 수신 정보 생성
  @Override
  @PostMapping
  public ResponseEntity<ReadStatusDto> createByChannelId(
      @RequestBody @Valid ReadStatusCreateRequest request) {
    ReadStatusDto response = readStatusService.create(request);
    return ResponseEntity.ok(response);
  }

  // 특정 채널의 메시지 수신 정보를 수정
  @Override
  @PatchMapping("/{readStatusId}")
  public ResponseEntity<ReadStatusDto> updateByChannelId(@PathVariable("readStatusId") UUID id,
      @RequestBody @Valid ReadStatusUpdateRequest request) {
    ReadStatusDto response = readStatusService.update(id, request);
    return ResponseEntity.ok(response);
  }

  // 특정 사용자의 메시지 수신 정보를 조회
  @Override
  @GetMapping
  public ResponseEntity<List<ReadStatusDto>> findAllByUserId(@RequestParam UUID userId) {
    List<ReadStatusDto> response = readStatusService.findAllByUserId(userId);
    return ResponseEntity.ok(response);
  }
}