package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.jwt.RequiresAuth;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.servlet.http.HttpServletRequest; // 필요한 경우 사용
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/read-status") // API 기본 경로 설정
@RequiredArgsConstructor
public class ReadStatusController {


    private final ReadStatusService readStatusService;

    @RequiresAuth
    @PostMapping("/create")
    public ResponseEntity<ReadStatusDto.ResponseReadStatus> createReceptionStatus(
            @RequestBody ReadStatusDto.Create request) {

        return ResponseEntity.ok(readStatusService.create(request));
    }

    @RequiresAuth
    @PutMapping("/update/{readStatusId}")
    public ResponseEntity<ReadStatusDto.ResponseReadStatus> updateReceptionStatus(
            @PathVariable UUID readStatusId,
            @Valid @RequestBody ReadStatusDto.Update request) {

        ReadStatusDto.ResponseReadStatus response = readStatusService.update(readStatusId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ReadStatusDto.ResponseReadStatus>> getUserReceptionStatuses(
            @PathVariable UUID userId) {

        return ResponseEntity.ok(readStatusService.findAllByUserId(userId));
    }


}