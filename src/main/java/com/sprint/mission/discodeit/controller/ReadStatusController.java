package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.status.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.status.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.status.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/read-status")
@RequiredArgsConstructor
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    // 메시지 수신 정보 생성
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ReadStatusResponse> create(@RequestBody CreateReadStatusRequest request) {
        UUID id = readStatusService.create(request).getId();
        return readStatusService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    // 메시지 수신 정보 수정
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@RequestBody UpdateReadStatusRequest request) {
        readStatusService.update(request);
        return ResponseEntity.ok().build();
    }

    // 특정 사용자의 메시지 수정 정보 목록 조회
    @RequestMapping(method = RequestMethod.GET, path = "/user/{userId}")
    public ResponseEntity<List<ReadStatusResponse>> findbyUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
    }


}
