package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.readStatus.request.ReadStatusCreatRequest;
import com.sprint.mission.discodeit.dto.readStatus.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.readStatus.response.ReadStatusFindAllByUserIdResponse;
import com.sprint.mission.discodeit.dto.readStatus.response.ReadStatusResponse;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/channel/readStatus")
public class ReadStatusController {
    private final ReadStatusService readStatusService;

    @PostMapping
    public ResponseEntity<ReadStatusResponse> readStatusCreate(
            @RequestBody @Validated ReadStatusCreatRequest readStatusCreatRequest){

        readStatusService.create(readStatusCreatRequest);
        return ResponseEntity.ok(new ReadStatusResponse(true, "읽기 상태 생성 성공"));
    }

    @PatchMapping
    public ResponseEntity<ReadStatusResponse> readStatusUpdate(
            @RequestBody @Validated ReadStatusUpdateRequest readStatusUpdateRequest
    ){
        readStatusService.update(readStatusUpdateRequest);
        return ResponseEntity.ok(new ReadStatusResponse(true, "읽기 상태 업데이트 성공"));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ReadStatusFindAllByUserIdResponse>> readStatusFindByUser(
            @PathVariable("userId") UUID userId
    ){
        return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
    }

}
