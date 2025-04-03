package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ApiDataResponse;
import com.sprint.mission.discodeit.dto.FindBinaryContentRequestDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @GetMapping("/{binaryContentId}")
  public ResponseEntity<ApiDataResponse<FindBinaryContentRequestDto>> find(
      @PathVariable UUID binaryContentId
  ) {
    binaryContentService.findById(binaryContentId);
    return ResponseEntity.ok(
        ApiDataResponse.success(binaryContentService.findById(binaryContentId)));
  }

  @GetMapping("/find")
  public ResponseEntity<ApiDataResponse<List<FindBinaryContentRequestDto>>> findByIdIn(
      @RequestParam List<UUID> binaryContentIdList
  ) {
    List<FindBinaryContentRequestDto> result = binaryContentService.findByIdIn(binaryContentIdList);
    return ResponseEntity.ok(ApiDataResponse.success(result));
  }
}
