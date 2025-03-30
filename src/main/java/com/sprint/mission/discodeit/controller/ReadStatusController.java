package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.dto.ReadStatusCreateDto;
import com.sprint.mission.discodeit.service.dto.ReadStatusResponseDto;
import com.sprint.mission.discodeit.service.dto.ReadStatusUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readstatus")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @PostMapping
    public ResponseEntity<ReadStatusResponseDto> create(@RequestBody ReadStatusCreateDto createDto) {
        ReadStatus readStatus = readStatusService.createReadStatus(createDto);
        ReadStatusResponseDto response = ReadStatusResponseDto.convertToResponseDto(readStatus);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReadStatusResponseDto> update(@RequestBody ReadStatusUpdateDto updateDto) {     //@PathVariable UUID id
        ReadStatus readStatus = readStatusService.updateReadStatus(updateDto);
        ReadStatusResponseDto response = ReadStatusResponseDto.convertToResponseDto(readStatus);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ReadStatusResponseDto>> getAllByUserId(@PathVariable UUID userId) {
        List<ReadStatusResponseDto> channelList = readStatusService.findAllByUserId(userId);
        return ResponseEntity.ok(channelList);
    }

}
