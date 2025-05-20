package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.status.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.status.ReadStatusDto;
import com.sprint.mission.discodeit.dto.status.ReadStatusResponse;
import com.sprint.mission.discodeit.dto.status.UpdateReadStatusRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    @Operation(summary = "읽음 상태 생성")
    @ApiResponse(responseCode = "201", description = "읽음 상태 생성 성공")
    public ResponseEntity<ReadStatusDto> create(
        @RequestBody @Valid CreateReadStatusRequest request) {
        ReadStatusDto readStatusDto = readStatusService.create(request);
        System.out.println("[DEBUG] Created ReadStatus ID = " + readStatusDto.id());
        return ResponseEntity.status(201).body(readStatusDto);
    }

    @Operation(summary = "읽음 상태 수정")
    @ApiResponse(
        responseCode = "200",
        description = "읽음 상태 수정 성공",
        content = @Content(mediaType = "*/*")
    )

    @PatchMapping("/{readStatusId}")
    public ResponseEntity<ReadStatusDto> update(
        @PathVariable UUID readStatusId,
        @RequestBody @Valid UpdateReadStatusRequest request
    ) {
        ReadStatusDto updated = readStatusService.update(readStatusId, request);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    @Operation(summary = "사용자의 모든 읽음 상태 조회")
    public ResponseEntity<List<ReadStatusDto>> findbyUserId(@RequestParam UUID userId) {
        return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
    }
}
