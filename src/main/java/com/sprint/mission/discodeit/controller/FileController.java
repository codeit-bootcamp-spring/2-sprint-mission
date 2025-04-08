package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Tag(name = "파일 API", description = "파일 업로드 및 다운로드 관련 기능을 제공합니다.")
@RestController
@RequiredArgsConstructor
public class FileController {

  private final BinaryContentService binaryContentService;

  @Operation(summary = "파일 업로드", description = "파일을 업로드하고 해당 파일의 ID를 반환합니다.")
  @PostMapping(value = "/api/files/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file)
      throws IOException {
    String fileName = file.getOriginalFilename();
    String contentType = file.getContentType();
    byte[] bytes = file.getBytes();

    BinaryContentCreateRequest request = new BinaryContentCreateRequest(fileName, contentType,
        bytes);
    BinaryContent savedBinaryContent = binaryContentService.create(request);

    return ResponseEntity.ok("파일이 업로드되었습니다. 파일 ID: " + savedBinaryContent.getId());
  }

  @Operation(summary = "파일 다운로드", description = "파일 ID를 통해 해당 파일을 다운로드합니다.")
  @GetMapping("/api/files/{fileId}")
  public ResponseEntity<Resource> getFile(@PathVariable UUID fileId) {
    BinaryContent content = binaryContentService.find(fileId);

    return ResponseEntity.ok()
        .contentType(MediaType.valueOf(content.getContentType()))
        .body(new ByteArrayResource(content.getBytes()));
  }

  @Operation(summary = "파일 목록 조회", description = "저장된 모든 파일의 ID 목록을 조회합니다.")
  @GetMapping("/api/files")
  public ResponseEntity<String> getFiles() {
    List<BinaryContent> files = binaryContentService.findAllByIdIn(List.of());

    if (files.isEmpty()) {
      return ResponseEntity.status(404).body("저장된 파일이 없습니다.");
    }

    String fileNames = files.stream()
        .map(binaryContent -> binaryContent.getId().toString())
        .collect(java.util.stream.Collectors.joining(", "));

    return ResponseEntity.ok("파일 목록: " + fileNames);
  }

  @Operation(summary = "파일 조회", description = "파일 ID를 사용하여 파일을 조회합니다.")
  @GetMapping("/api/files/find")
  public ResponseEntity<Resource> getBinaryContent(@RequestParam UUID binaryContentId) {
    BinaryContent content = binaryContentService.find(binaryContentId);

    return ResponseEntity.ok()
        .contentType(MediaType.valueOf(content.getContentType()))
        .body(new ByteArrayResource(content.getBytes()));
  }

  @Operation(summary = "첨부 파일 조회", description = "첨부 파일 ID를 통해 해당 파일 정보를 조회합니다.")
  @GetMapping("/api/binaryContents/{binaryContentId}")
  public ResponseEntity<BinaryContent> findBinaryContentById(@PathVariable UUID binaryContentId) {
    BinaryContent content = binaryContentService.find(binaryContentId);
    return ResponseEntity.ok(content);
  }

  @Operation(summary = "여러 첨부 파일 조회", description = "여러 첨부 파일 ID를 이용하여 목록을 조회합니다.")
  @GetMapping("/api/binaryContents")
  public ResponseEntity<List<BinaryContent>> findAllBinaryContents(
      @RequestParam List<UUID> binaryContentIds
  ) {
    List<BinaryContent> contents = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.ok(contents);
  }
}
