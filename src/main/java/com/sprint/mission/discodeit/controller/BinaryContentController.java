package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.BinaryContentApi;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")
public class BinaryContentController implements BinaryContentApi {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;

  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentDto> get(@PathVariable UUID binaryContentId) {
    log.info("바이너리 컨텐츠 조회 요청: id={}", binaryContentId);
    BinaryContentDto file = binaryContentService.findById(binaryContentId);
    log.debug("바이너리 컨텐츠 조회 응답: {}", file);
    return ResponseEntity.ok(file);
  }

  @GetMapping
  public ResponseEntity<List<BinaryContentDto>> getFiles(@RequestParam List<UUID> ids) {
    log.info("바이너리 컨텐츠 목록 조회 요청: ids={}", ids);
    List<BinaryContentDto> files = binaryContentService.findAllByIdIn(ids);
    log.debug("바이너리 컨텐츠 목록 조회 응답: count={}", files.size());
    return ResponseEntity.ok(files);
  }

  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<?> download(
      @PathVariable UUID binaryContentId
  ) {
    log.info("바이너리 컨텐츠 다운로드 요청: id={}", binaryContentId);
    BinaryContentDto dto = binaryContentService.findById(binaryContentId);
    ResponseEntity<?> response = binaryContentStorage.download(dto);
    log.debug("바이너리 컨텐츠 다운로드 응답: contentType={}, contentLength={}",
        response.getHeaders().getContentType(), response.getHeaders().getContentLength());
    return response;
  }

  
}
