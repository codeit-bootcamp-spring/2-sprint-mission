package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.common.ApiResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.common.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReadStatus>> create(@Valid @RequestBody ReadStatusCreateRequest request) {
        ReadStatus readStatus = readStatusService.create(request);
        ApiResponse<ReadStatus> response = new ApiResponse<>("체널의 메시지 읽음 상태 생성 성공", readStatus);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ReadStatus>> update(@Valid @RequestBody ReadStatusUpdateRequest request) {
        ReadStatus updatedStatus = readStatusService.update(request);
        ApiResponse<ReadStatus> response = new ApiResponse<>("체널의 메시지 읽음 상태 수정 성공", updatedStatus);
        return ResponseEntity.ok(response);
    }
}
