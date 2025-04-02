package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BaseResponseDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @PostMapping("/read-statuses")
    public ResponseEntity<BaseResponseDto> createReadStatus(@RequestBody ReadStatusCreateDto readStatusCreateDto) {
        ReadStatus readStatus = readStatusService.create(readStatusCreateDto);
        return ResponseEntity.ok(BaseResponseDto.success(readStatus.getId() + " ReadStatus 등록이 완료되었습니다."));
    }

    @PutMapping("/read-statuses/{id}")
    public ResponseEntity<BaseResponseDto> updateReadStatus(@PathVariable("id") UUID readStatusId,
                                                            @RequestBody ReadStatusUpdateDto readStatusUpdateDto) {
        ReadStatus readStatus = readStatusService.update(readStatusId, readStatusUpdateDto);
        return ResponseEntity.ok(BaseResponseDto.success(readStatus.getId() + " ReadStatus 변경이 완료되었습니다."));
    }

    @GetMapping("users/{userId}/read-statuses")
    public ResponseEntity<List<ReadStatus>> getReadStatuses(@PathVariable("userId") UUID userId) {
        return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
    }
}
