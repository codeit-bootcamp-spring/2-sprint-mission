package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readstatus")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @PostMapping("/create")
    public ResponseEntity<ReadStatus> create(@RequestBody ReadStatusCreateRequest request) {
        System.out.println("읽음 상태 최초 생성 API 실행");
        ReadStatus readStatus = readStatusService.create(request);
        System.out.println("readStatus :" + readStatus);
        return ResponseEntity.ok(readStatus);
    }

    @PutMapping("/{readStatusId}")
    public ResponseEntity<ReadStatus> update(
            @PathVariable UUID readStatusId,
            @RequestBody ReadStatusUpdateRequest request) {
        System.out.println("수신 정보 업데이트 API 실행");
        ReadStatus readStatus = readStatusService.update(readStatusId, request);
        return ResponseEntity.ok(readStatus);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ReadStatus>> findAll(@PathVariable UUID userId) {
        System.out.println("수신 정보 전체 조회 API 실행");
        List<ReadStatus> readStatusList = readStatusService.findAllByUserId(userId);
        System.out.println("참여 톡방 읽음 상태 리스트 : " + readStatusList);
        return ResponseEntity.ok(readStatusList);
    }

}
