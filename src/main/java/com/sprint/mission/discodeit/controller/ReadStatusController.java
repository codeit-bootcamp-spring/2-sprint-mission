package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/read-status")
@RequiredArgsConstructor
public class ReadStatusController {

    private final ReadStatusService readStatusService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<Void>> save(@RequestBody SaveReadStatusParamDto saveReadStatusParamDto) {
        readStatusService.save(saveReadStatusParamDto);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody UpdateReadStatusParamDto updateReadStatusParamDto) {
        readStatusService.update(updateReadStatusParamDto);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PostMapping("/find")
    public ResponseEntity<ApiResponse<List<CheckReadStatusResponseDto>>> findByUserId(@RequestBody FindReadStatusByUserIdRequestDto findReadStatusByUserIdRequestDto) {
        List<CheckReadStatusResponseDto> checkReadStatusResponseDtoList = readStatusService.findAllByUserId(findReadStatusByUserIdRequestDto);
        return ResponseEntity.ok(ApiResponse.success(checkReadStatusResponseDtoList));
    }
}
