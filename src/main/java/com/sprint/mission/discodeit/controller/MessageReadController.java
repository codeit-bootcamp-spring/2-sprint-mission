package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.MessageReadApi;
import com.sprint.mission.discodeit.controller.dto.ReadStatusResponse;
import com.sprint.mission.discodeit.controller.dto.UserReadStatusResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
public class MessageReadController implements MessageReadApi {

  private final ReadStatusService readStatusService;

  // 특정 채널의 메시지 수신 정보 생성
  @Override
  @PostMapping
  public ResponseEntity<ReadStatusResponse> createByChannelId(
      @RequestBody ReadStatusCreateRequest request) {
    ReadStatus status = readStatusService.create(request);
    return ResponseEntity.ok(ReadStatusResponse.of(status));
  }

  // 특정 채널의 메시지 수신 정보를 수정
  @Override
  @PatchMapping("/{readStatusId}")
  public ResponseEntity<ReadStatusResponse> updateByChannelId(@PathVariable("readStatusId") UUID id,
      @RequestBody ReadStatusUpdateRequest request) {
    ReadStatus status = readStatusService.update(id, request);
    return ResponseEntity.ok(ReadStatusResponse.of(status));
  }

  // 특정 사용자의 메시지 수신 정보를 조회
  @Override
  @GetMapping
  public ResponseEntity<List<UserReadStatusResponse>> findAllByUserId(@RequestParam UUID userId) {
    List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
    List<UserReadStatusResponse> response = readStatuses.stream()
        .map(UserReadStatusResponse::of).toList();
    return ResponseEntity.ok(response);
  }
}