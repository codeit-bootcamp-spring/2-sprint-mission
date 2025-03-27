package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.dto.IdResponse;
import com.sprint.mission.discodeit.controller.dto.UserReadStatusResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.service.dto.readstatus.ReadStatusUpdateRequest;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/message-read-status")
public class MessageReadController {
    private final ReadStatusService readStatusService;

    // 특정 채널의 메시지 수신 정보 생성
    @RequestMapping(value = "/{channelId}", method = RequestMethod.POST)
    public ResponseEntity<IdResponse> createByChannelId(@PathVariable UUID channelId,
                                                        @RequestBody ReadStatusCreateRequest request) {
        ReadStatus status = readStatusService.create(channelId, request);
        return ResponseEntity.ok(IdResponse.of(true, status.getId()));
    }

    // 특정 채널의 메시지 수신 정보를 수정
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<IdResponse> updateByChannelId(@PathVariable("id") UUID id,
                                                        @RequestBody ReadStatusUpdateRequest request) {
        ReadStatus status = readStatusService.update(id, request);
        return ResponseEntity.ok(IdResponse.of(true, status.getId()));
    }

    // 특정 사용자의 메시지 수신 정보를 조회
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity<List<UserReadStatusResponse>> findAllByUserId(@PathVariable UUID userId) {
        List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
        List<UserReadStatusResponse> response = readStatuses.stream()
                .map(UserReadStatusResponse::of).toList();
        return ResponseEntity.ok(response);
    }
}
