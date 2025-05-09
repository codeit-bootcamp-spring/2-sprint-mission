package com.sprint.mission.discodeit.readstatus.controller;

import com.sprint.mission.discodeit.readstatus.dto.ReadStatusResult;
import com.sprint.mission.discodeit.readstatus.service.ReadStatusService;
import com.sprint.mission.discodeit.readstatus.dto.request.ReadStatusCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatuses")
@Tag(name = "ReadStatus", description = "채널내 메세지 읽음 상태 관련 API")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @Operation(
            summary = "읽음 상태 생성",
            description = "채널안 유저의 메세지 읽음 상태 생성"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "읽음 상태 등록 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
    @PostMapping
    public ResponseEntity<ReadStatusResult> create(
            @Parameter(description = "읽음 상태 생성 정보", required = true)
            @Valid @RequestBody ReadStatusCreateRequest request) {
        ReadStatusResult readStatusResult = readStatusService.create(request);

        return ResponseEntity.ok(readStatusResult);
    }

    @Operation(
            summary = "읽음 상태 수정",
            description = "최근 시간으로 읽음 상태 수정"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "읽음 상태 수정 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
    @PatchMapping("/{readStatusId}")
    public ResponseEntity<ReadStatusResult> update(
            @Parameter(description = "읽음 상태 ID", required = true)
            @PathVariable UUID readStatusId) {
        ReadStatusResult readStatusResult = readStatusService.updateLastReadTime(readStatusId);

        return ResponseEntity.ok(readStatusResult);
    }

    @Operation(
            summary = "읽음 상태 USER_ID 조회",
            description = "유저가 속한 모든 채널의 읽음 상태를 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "읽음 상태 조회 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
    @GetMapping
    public ResponseEntity<List<ReadStatusResult>> getAllByUserId(
            @Parameter(description = "유저 ID", required = true)
            @RequestParam UUID userId) {

        List<ReadStatusResult> readStatusResults = readStatusService.getAllByUserId(userId);

        return ResponseEntity.ok(readStatusResults);
    }

    @Operation(
            summary = "읽음 상태 삭제"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "읽음 상태 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "파라미터 오류")
    })
    @DeleteMapping("/{readStatusId}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "읽음 상태 ID", required = true)
            @PathVariable UUID readStatusId) {

        readStatusService.delete(readStatusId);

        return ResponseEntity.noContent().build();
    }
}