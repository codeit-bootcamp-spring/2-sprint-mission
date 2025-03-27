package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.common.ApiResponse;
import com.sprint.mission.discodeit.dto.readstatus.*;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/readStatus")
@RequiredArgsConstructor
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ReadStatusCreateResponse>> create(@Valid @RequestBody ReadStatusCreateRequest request) {
        UUID readStatusId = readStatusService.createReadStatus(request);
        ApiResponse<ReadStatusCreateResponse> response = new ApiResponse<>(true, "readStatus 생성 성공", new ReadStatusCreateResponse(readStatusId, request.userId(), request.channelId()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<ReadStatusUpdateResponse>> update(@Valid @RequestBody ReadStatusUpdateRequest request) {
        readStatusService.updateReadStatus(request);
        ApiResponse<ReadStatusUpdateResponse> response = new ApiResponse<>(true, "readStatus 업데이트 성공", new ReadStatusUpdateResponse(request.userId(), request.channelId()));
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @PostMapping("/read")
    public ResponseEntity<ApiResponse<ReadStatusReadResponse>> read(@Valid @RequestBody ReadStatusReadRequest request) {
        ReadStatusReadResponse readResponse = readStatusService.findReadStatusByUserIdChannelID(request.userId(), request.channelId());
        ApiResponse<ReadStatusReadResponse> response = new ApiResponse<>(true, "readStatus 조회 성공", readResponse);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
