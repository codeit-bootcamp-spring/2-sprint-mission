package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.dto.readstatusdto.ReadStatusCreateDto;
import com.sprint.mission.discodeit.service.dto.readstatusdto.ReadStatusResponseDto;
import com.sprint.mission.discodeit.service.dto.readstatusdto.ReadStatusUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @PostMapping
    @Operation(summary = "Message 읽음 상태 생성")
    @ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음", content = @Content(examples = @ExampleObject(value = "User or Channel not found")))
    @ApiResponse(responseCode = "400", description = "이미 읽은 상태가 존재함", content = @Content(examples = @ExampleObject(value = "Read status already exists")))
    @ApiResponse(responseCode = "201", description = "Message 읽음 상태가 성공적으로 생성됨")
    public ResponseEntity<ReadStatusResponseDto> create(
            @RequestBody ReadStatusCreateDto readStatusCreateRequest
    ) {
        ReadStatusResponseDto createReadStatus = readStatusService.create(readStatusCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createReadStatus);
    }


    @PatchMapping("/{readStatusId}")
    @Operation(summary = "Message 읽음 상태 수정")
    @ApiResponse(responseCode = "200", description = "Message 읽음 상태가 성공적으로 수정됨")
    @ApiResponse(responseCode = "404", description = "Message 읽음 상태를 찾을 수 없음", content = @Content(examples = @ExampleObject(value = "Read status not found")))
    public ResponseEntity<ReadStatusResponseDto> update(
            @PathVariable @Parameter(description = "수정 할 읽음 상태 ID") UUID readStatusId,
            @RequestBody ReadStatusUpdateDto readStatusUpdateRequest
    ) {
        ReadStatusResponseDto updateReadStatus = readStatusService.update(readStatusId, readStatusUpdateRequest);
        return ResponseEntity.ok(updateReadStatus);
    }


    @GetMapping
    @Operation(summary = "User의 Message 읽음 상태 목록 조회")
    @ApiResponse(responseCode = "200", description = "Message 읽음 상태 목록 조회 성공")
    public ResponseEntity<List<ReadStatusResponseDto>> findAllByUserId(
            @RequestParam @Parameter(description = "조회할 User ID") UUID userId
    ) {
        List<ReadStatusResponseDto> findAllByUserId = readStatusService.findAllByUserId(userId);
        return ResponseEntity.ok(findAllByUserId);
    }
}
