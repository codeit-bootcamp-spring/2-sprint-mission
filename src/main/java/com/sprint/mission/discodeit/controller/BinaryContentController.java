package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.controller.binarycontent.FindBinaryContentResponseDTO;
import com.sprint.mission.discodeit.dto.service.binarycontent.FindBinaryContentResult;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.swagger.BinaryContentApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
public class BinaryContentController implements BinaryContentApi {

  private final BinaryContentService binaryContentService;
  private final BinaryContentMapper binaryContentMapper;

  @Override
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<FindBinaryContentResponseDTO> getBinaryContent(
      @PathVariable("binaryContentId") UUID id) {
    FindBinaryContentResult findBinaryContentResult = binaryContentService.find(id);
    FindBinaryContentResponseDTO binaryContent = binaryContentMapper.toFindBinaryContentResponseDTO(
        findBinaryContentResult);
    return ResponseEntity.ok(binaryContent);
  }

  @Override
  @GetMapping
  public ResponseEntity<List<FindBinaryContentResponseDTO>> getBinaryContentAllIn(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
    List<FindBinaryContentResult> binaryContents = binaryContentService.findAllByIdIn(
        binaryContentIds);
    List<FindBinaryContentResponseDTO> findBinaryContentResponseDTOList = binaryContents.stream()
        .map(binaryContentMapper::toFindBinaryContentResponseDTO)
        .toList();
    return ResponseEntity.ok(findBinaryContentResponseDTOList);
  }
}
