package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readStatus")
@RequiredArgsConstructor
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @PostMapping("")
  public ResponseEntity<ApiDataResponse<Void>> save(
      @RequestBody SaveReadStatusParamDto saveReadStatusParamDto
  ) {
    readStatusService.save(saveReadStatusParamDto);
    return ResponseEntity.ok(ApiDataResponse.success());
  }

  @PutMapping("/update")
  public ResponseEntity<ApiDataResponse<Void>> update(
      @RequestBody UpdateReadStatusParamDto updateReadStatusParamDto) {
    readStatusService.update(updateReadStatusParamDto);
    return ResponseEntity.ok(ApiDataResponse.success());
  }

  @GetMapping("/find")
  public ResponseEntity<ApiDataResponse<List<CheckReadStatusResponseDto>>> findByUserId(
      @RequestBody FindReadStatusByUserIdRequestDto findReadStatusByUserIdRequestDto
  ) {
    List<CheckReadStatusResponseDto> checkReadStatusResponseDtoList = readStatusService.findAllByUserId(
        findReadStatusByUserIdRequestDto);
    return ResponseEntity.ok(
        ApiDataResponse.success(checkReadStatusResponseDtoList));
  }
}
