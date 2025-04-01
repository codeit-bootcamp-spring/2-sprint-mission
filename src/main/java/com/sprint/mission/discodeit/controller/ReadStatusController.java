package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto2.response.ApiResponse;
import com.sprint.mission.discodeit.dto2.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto2.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readstatus")
@RequiredArgsConstructor
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    // 1. 특정 채널의 메시지 수신 정보 생성
    @RequestMapping(value = "/channel", method = RequestMethod.POST)
    public ResponseEntity<ApiResponse<UUID>> createReceipts(@RequestBody ReadStatusCreateRequest request) {
        UUID id = readStatusService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Receipt info created for channel: " + request.channelId(), id));
    }

    // 2. 특정 채널의 메시지 수신 정보 수정
    @RequestMapping(value = "/channel", method = RequestMethod.PUT)
    public ResponseEntity<ApiResponse<Void>> updateReceipts(@RequestBody ReadStatusUpdateRequest request) {
        readStatusService.update(request);
        return ResponseEntity.ok(new ApiResponse<>("Receipt info updated for channel: " + request.channelId(), null));

    }

    // 3. 특정 사용자의 메시지 수신 정보 조회
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<ApiResponse<List<ReadStatus>>> getReceiptsByUser(@PathVariable UUID userId) {
        List<ReadStatus> readstatus = readStatusService.findAllByUserId(userId);
        ApiResponse<List<ReadStatus>> response = new ApiResponse<>("Successfully retrieved message reception information.", readstatus);
        return ResponseEntity.ok(response);
    }
}
