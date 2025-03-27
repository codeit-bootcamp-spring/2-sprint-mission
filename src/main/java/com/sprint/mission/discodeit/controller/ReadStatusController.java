package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.common.ApiResponse;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/readStatus")
@RequiredArgsConstructor
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ReadStatusCreateResponse>> readStatus(@Valid @RequestBody ReadStatusCreateRequest request) {
        UUID readStatusId = readStatusService.createReadStatus(request);
        ApiResponse<ReadStatusCreateResponse> response = new ApiResponse<>(true, "readStatus 생성 성공", new ReadStatusCreateResponse(readStatusId, request.userId(), request.channelId()));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }


}
