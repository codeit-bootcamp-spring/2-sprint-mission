package com.sprint.mission.discodeit.core.content.controller;

import static com.sprint.mission.discodeit.core.content.controller.BinaryContentDtoMapper.toCreateResponse;

import com.sprint.mission.discodeit.core.content.controller.dto.BinaryContentDto;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.port.BinaryContentStoragePort;
import com.sprint.mission.discodeit.core.content.usecase.BinaryContentService;
import com.sprint.mission.discodeit.swagger.BinaryContentApi;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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

  private final BinaryContentStoragePort binaryContentStorage;
  private final BinaryContentService binaryContentService;

  @GetMapping
  public ResponseEntity<List<BinaryContentDto>> findAllByIdIn(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
    List<BinaryContent> allByIdIn = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.ok(allByIdIn.stream()
        .map(BinaryContentDtoMapper::toCreateResponse).toList());
  }

  @GetMapping("/{binaryContentId}")
  public ResponseEntity<BinaryContentDto> find(
      @PathVariable UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentService.findById(binaryContentId);
    return ResponseEntity.ok(toCreateResponse(binaryContent));
  }

  @GetMapping("/{binaryContentId}/download")
  public ResponseEntity<?> download(@PathVariable UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentService.findById(binaryContentId);
    BinaryContentDto response = toCreateResponse(binaryContent);
    return binaryContentStorage.download(response);
  }
}
