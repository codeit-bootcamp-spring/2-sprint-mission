package com.sprint.mission.discodeit.domain.binarycontent.controller;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentResult;
import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentService;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<BinaryContentResult> create(@RequestPart MultipartFile multipartFile) {
    BinaryContentRequest binaryContentRequest = BinaryContentRequest.from(multipartFile);
    BinaryContentResult binaryContentResult = binaryContentService.createBinaryContent(
        binaryContentRequest);

    return ResponseEntity.ok(binaryContentResult);
  }

  @GetMapping
  public ResponseEntity<List<BinaryContentResult>> getByIdIn(
      @RequestParam(value = "binaryContentIds") List<UUID> binaryContentIds) {
    List<BinaryContentResult> binaryContentResults = binaryContentService.getByIdIn(
        binaryContentIds);

    return ResponseEntity.ok(binaryContentResults);
  }

  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentResult> getById(
      @PathVariable(value = "binaryContentId") UUID binaryContentId) {
    BinaryContentResult binaryContentResult = binaryContentService.getById(binaryContentId);

    return ResponseEntity.ok().body(binaryContentResult);
  }

  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<?> download(@PathVariable(value = "binaryContentId") UUID binaryContentId) {
    return binaryContentStorage.download(binaryContentId);
  }

  @DeleteMapping("/{binaryContentId}")
  public ResponseEntity<Void> delete(@NotNull @PathVariable UUID binaryContentId) {
    binaryContentService.delete(binaryContentId);

    return ResponseEntity.noContent().build();
  }

}
