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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/read-status")
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BaseResponseDto> createReadStatus(@RequestBody ReadStatusCreateDto readStatusCreateDto) {
        ReadStatus readStatus = readStatusService.create(readStatusCreateDto);
        return ResponseEntity.ok(BaseResponseDto.success(readStatus.getId() + " ReadStatus 등록이 완료되었습니다."));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<BaseResponseDto> updateReadStatus(@RequestBody ReadStatusUpdateDto readStatusUpdateDto) {
        ReadStatus readStatus = readStatusService.update(readStatusUpdateDto);
        return ResponseEntity.ok(BaseResponseDto.success(readStatus.getId() + " ReadStatus 변경이 완료되었습니다."));
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<ReadStatus>> getReadStatuss(@PathVariable("id") UUID userId) {
        return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
    }
}
