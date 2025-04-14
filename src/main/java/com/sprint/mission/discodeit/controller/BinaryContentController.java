package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.BinaryContentApi;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.dto.binarycontent.BinaryContentResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentApi {

  private final BinaryContentService binaryContentService;

  // 파일 조회
  @Override
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentResponse> find(
      @PathVariable UUID binaryContentId) {
    BinaryContentResponse response = binaryContentService.findById(binaryContentId);
    return ResponseEntity.ok(response);
  }

  // 여러 파일 조회
  @Override
  @GetMapping
  public ResponseEntity<List<BinaryContentResponse>> findAll(
      @RequestParam List<UUID> binaryContentIds) {
    List<BinaryContentResponse> response = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.ok(response);
  }

  // 파일 다운로드
  @Override
  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<Resource> download(@PathVariable UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentService.findContentById(binaryContentId);
    ByteArrayResource resource = new ByteArrayResource(binaryContent.getBytes());

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .header(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"" + binaryContent.getFileName() + "\"")
        .body(resource);
  }
}