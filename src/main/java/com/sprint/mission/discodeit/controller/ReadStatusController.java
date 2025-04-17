package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/readStatuses")
@Tag(name = "ReadStatus", description = "Message 읽음 상태 API")
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @Operation(summary = "Message 읽음 상태 생성")
    @PostMapping
    public ResponseEntity<ReadStatusDto> createReadStatus(@RequestBody ReadStatusCreateDto readStatusCreateDto) {
        ReadStatusDto readStatus = readStatusService.create(readStatusCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(readStatus);
    }

    @Operation(summary = "Message 읽음 상태 수정")
    @PatchMapping("/{readStatusId}")
    public ResponseEntity<ReadStatusDto> updateReadStatus(@PathVariable UUID readStatusId,
                                                          @RequestBody ReadStatusUpdateDto readStatusUpdateDto) {
        ReadStatusDto readStatus = readStatusService.update(readStatusId, readStatusUpdateDto);
        return ResponseEntity.ok(readStatus);
    }

    @Operation(summary = "User의 Message 읽음 상태 목록 조회")
    @GetMapping
    public ResponseEntity<List<ReadStatusDto>> getReadStatuses(@RequestParam(name = "userId") UUID userId) {
        return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
    }
}
