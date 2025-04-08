package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
@Tag(name = "BinaryContent", description = "첨부 파일 API")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @Operation(summary = "여러 첨부 파일 조회")
  @GetMapping()
  public ResponseEntity<List<BinaryContent>> findAllById(
      @RequestParam List<UUID> binaryIds
  ) {
    System.out.println("바이너리 여러 파일 조회 API 실행");
    List<BinaryContent> binaryContentList = binaryContentService.findAllByIdIn(binaryIds);
    System.out.println("binary list :" + binaryContentList);
    return ResponseEntity.ok(binaryContentList);
  }

  @Operation(summary = "첨부 파일 조회")
  @GetMapping("/{BinaryContentId}")
  public ResponseEntity<BinaryContent> findBinary(@PathVariable UUID BinaryContentId) {
    System.out.println("바이너리 단일(프로필) 파일 조회 API 실행");
    BinaryContent binaryContent = binaryContentService.find(BinaryContentId);
    System.out.println("binary Content(profile) :" + binaryContent);
    return ResponseEntity.ok(binaryContent);
  }
}
