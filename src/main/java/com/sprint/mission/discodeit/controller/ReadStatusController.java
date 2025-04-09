package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
@Tag(name = "ReadStatus")
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @Operation(summary = "Message 읽음 상태 생성", operationId = "create_1")
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "Channel 또는 User를 찾을 수 없음"),
            @ApiResponse(responseCode = "400", description = "이미 읽음 상태가 존재함"),
            @ApiResponse(responseCode = "201", description = "Message 읽음 상태가 성공적으로 생성됨")
    })
    @PostMapping
    public ResponseEntity<ReadStatus> create(@RequestBody ReadStatusCreateRequest request) {
        ReadStatus readStatus = readStatusService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(readStatus);
    }

    @Operation(summary = "User의 Message 읽음 상태 목록 조회", operationId = "findAllByUserId")
    @ApiResponse(responseCode = "200", description = "Message 읽음 상태 목록 조회 성공")
    @GetMapping
    public ResponseEntity<List<ReadStatus>> findAllByUserId(
            @Parameter(
                    name = "userId",
                    description = "조회할 User ID",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid")
            )
            @RequestParam UUID userId) {
        List<ReadStatus> statuses = readStatusService.findAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(statuses);
    }

    @Operation(summary = "Message 읽음 상태 수정", operationId = "update_1")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Message 읽음 상태가 성공적으로 수정됨"),
            @ApiResponse(responseCode = "404", description = "Message 읽음 상태를 찾을 수 없음")
    })
    @PatchMapping("/{readStatusId}")
    public ResponseEntity<ReadStatus> updateReadStatusById(
            @Parameter(
                    name = "readStatusId",
                    description = "수정할 읽음 상태 ID",
                    required = true,
                    schema = @Schema(type = "string", format = "uuid")
            )
            @PathVariable UUID readStatusId,
            @RequestBody ReadStatusUpdateRequest request) {
        ReadStatus updatedReadStatus = readStatusService.update(readStatusId, request);
        return ResponseEntity.status(HttpStatus.OK).body(updatedReadStatus);
    }
}
