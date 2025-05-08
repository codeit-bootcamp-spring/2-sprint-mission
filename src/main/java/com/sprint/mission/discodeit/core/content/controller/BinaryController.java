package com.sprint.mission.discodeit.core.content.controller;

import static com.sprint.mission.discodeit.core.content.controller.BinaryContentDtoMapper.toCreateResponse;

import com.sprint.mission.discodeit.core.content.controller.response.BinaryContentResponse;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.port.BinaryContentStoragePort;
import com.sprint.mission.discodeit.core.content.usecase.BinaryContentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Binary Content", description = "바이너리 데이터 관련 API")
@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryController {

  private final BinaryContentStoragePort binaryContentStorage;
  private final BinaryContentService binaryContentService;

  @GetMapping
  public ResponseEntity<List<BinaryContentResponse>> findAllBinaryContent(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
    List<BinaryContent> allByIdIn = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.ok(allByIdIn.stream()
        .map(BinaryContentDtoMapper::toCreateResponse).toList());
  }

  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentResponse> findBinaryContent(
      @PathVariable UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentService.findById(binaryContentId);
    return ResponseEntity.ok(toCreateResponse(binaryContent));
  }

  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<?> download(@PathVariable UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentService.findById(binaryContentId);
    BinaryContentResponse response = toCreateResponse(binaryContent);
    return binaryContentStorage.download(response);
  }
}
