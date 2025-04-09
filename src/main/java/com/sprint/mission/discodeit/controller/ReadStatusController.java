package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/readStatus")
@Tag(name = "ReadStatus", description = "읽음 상태 관리 API")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @Operation(summary = "readStatus 생성", description = "사용자와 채널에 대한 읽음 상태를 생성합니다.")
    @ApiResponse(
            responseCode = "201",
            description = "생성 성공",
            content = @Content(schema = @Schema(implementation = ReadStatus.class))
    )
    @ApiResponse(
            responseCode = "400",
            description = "잘못된 요청 데이터",
            content = @Content(examples = @ExampleObject(value = "유효하지 않은 요청 데이터"))
    )
    @PostMapping
    public ResponseEntity<ReadStatus> create(
            @Parameter(description = "읽음 상태 생성 요청 데이터", required = true)
            @RequestBody ReadStatusCreateRequest request) {
        ReadStatus createdReadStatus = readStatusService.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdReadStatus);
    }

    @Operation(summary = "readStatus 수정", description = "사용자와 채널에 대한 읽음 상태를 업데이트 합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "수정 성공",
            content = @Content(schema = @Schema(implementation = ReadStatus.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "읽음 상태를 찾을 수 없습니다.",
            content = @Content(examples = @ExampleObject(value = "ReadStatus with ID {readStatusId} not found"))
    )
    @PutMapping("/{readStatusId}")
    public ResponseEntity<ReadStatus> update(
            @Parameter(description = "수정할 readStatusId", required = true)
            @PathVariable("readStatusId") UUID readStatusId,

            @Parameter(description = "읽음 상태 수정 요청 데이터", required = true)
            @RequestBody ReadStatusUpdateRequest request
    ) {
        ReadStatus updatedReadStatus = readStatusService.update(readStatusId, request);
        return ResponseEntity.ok(updatedReadStatus);
    }

    @Operation(summary = "사용자의 모든 읽음 상태 조회", description = "특정 사용자의 모든 읽음 상태를 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(schema = @Schema(type = "array", implementation = ReadStatus.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "사용자를 찾을 수 없음",
            content = @Content(examples = @ExampleObject(value = "User with ID {userId} not found"))
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReadStatus>> findAllByUserId(@PathVariable UUID userId) {
        List<ReadStatus> readStatuses = readStatusService.findAllByUserId(userId);
        return ResponseEntity.ok(readStatuses);
    }
}
