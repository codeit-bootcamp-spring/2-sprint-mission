package com.sprint.mission.discodeit.domain.readstatus.controller;

import com.sprint.mission.discodeit.domain.readstatus.service.ReadStatusService;
import com.sprint.mission.discodeit.domain.readstatus.dto.ReadStatusResult;
import com.sprint.mission.discodeit.domain.readstatus.dto.request.ReadStatusCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;
    // public을 만들떄 readStatus를 만드는가? -> 아님
    // 아이게 메세지를 보내서 그런건가?

    @PostMapping // public도 읽음 상태가 있을 수 있으니깐 인정, 읽은 상태 생성으로 가는게 맞나 이게?, 근데 public도 만들어준이
    public ResponseEntity<ReadStatusResult> create(@Valid @RequestBody ReadStatusCreateRequest request) {
        log.info("읽음 상태 생성 요청 수신: channelId={}, userId={}", request.channelId(), request.userId());
        ReadStatusResult readStatusResult = readStatusService.create(request);
        log.info("읽음 상태 생성 완료: channelId={}, userId={}, result={}", request.channelId(), request.userId(), readStatusResult);

        return ResponseEntity.ok(readStatusResult);
    }

    @PatchMapping("/{readStatusId}")
    public ResponseEntity<ReadStatusResult> update(@PathVariable UUID readStatusId) {
        ReadStatusResult readStatusResult = readStatusService.updateLastReadTime(readStatusId, Instant.now());

        return ResponseEntity.ok(readStatusResult);
    }

    @GetMapping
    public ResponseEntity<List<ReadStatusResult>> getAllByUserId(@RequestParam UUID userId) {
        List<ReadStatusResult> readStatusResults = readStatusService.getAllByUserId(userId);

        return ResponseEntity.ok(readStatusResults);
    }

    @DeleteMapping("/{readStatusId}")
    public ResponseEntity<Void> delete(@PathVariable UUID readStatusId) {
        readStatusService.delete(readStatusId);

        return ResponseEntity.noContent().build();
    }

}