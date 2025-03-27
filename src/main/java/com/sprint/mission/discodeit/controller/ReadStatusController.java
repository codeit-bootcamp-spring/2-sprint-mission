package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sprint.mission.discodeit.dto.CheckReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.FindReadStatusByUserIdRequestDto;
import com.sprint.mission.discodeit.dto.SaveReadStatusParamDto;
import com.sprint.mission.discodeit.dto.UpdateReadStatusParamDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/read-status")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody SaveReadStatusParamDto saveReadStatusParamDto) {
        readStatusService.save(saveReadStatusParamDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody UpdateReadStatusParamDto updateReadStatusParamDto) {
        readStatusService.update(updateReadStatusParamDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/find")
    public ResponseEntity<List<CheckReadStatusResponseDto>> findByUserId(@RequestBody FindReadStatusByUserIdRequestDto findReadStatusByUserIdRequestDto) {
        List<CheckReadStatusResponseDto> checkReadStatusResponseDtoList = readStatusService.findAllByUserId(findReadStatusByUserIdRequestDto);
        return ResponseEntity.ok().body(checkReadStatusResponseDtoList);
    }
}
