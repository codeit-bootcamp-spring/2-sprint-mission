package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.jwt.RequiresAuth;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.validation.Valid;
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