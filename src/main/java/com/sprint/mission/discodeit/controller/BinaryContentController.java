package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.controller.binarycontent.BinaryContentListDTO;
import com.sprint.mission.discodeit.dto.controller.binarycontent.FindAllBinaryContentInRequestDTO;
import com.sprint.mission.discodeit.dto.controller.binarycontent.FindBinaryContentResponseDTO;
import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDTO;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Binary-Content-Controller", description = "BinaryContent 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binaryContents")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;
  private final BinaryContentMapper binaryContentMapper;

  @Operation(summary = "BinaryContent 단건 조회",
      description = "binaryContentId에 해당하는 BinaryContent를 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "BinaryContent 조회 성공"),
          @ApiResponse(responseCode = "404", description = "binaryContentId에 해당하는 BinaryContnet가 존재하지 않음")
      })
  @GetMapping("/{binaryContentId}")
  public ResponseEntity<FindBinaryContentResponseDTO> getBinaryContent(
      @PathVariable("binaryContentId") UUID id) {
    BinaryContentDTO binaryContentDTO = binaryContentService.find(id);
    FindBinaryContentResponseDTO binaryContent = binaryContentMapper.toBinaryContentResponseDTO(
        binaryContentDTO);
    return ResponseEntity.ok(binaryContent);
  }

  @Operation(summary = "BinaryContent 다건 조회",
      description = "binaryContentIds에 해당하는 BinaryContent들을 조회합니다.",
      responses = {
          @ApiResponse(responseCode = "200", description = "BinaryContent 다건 조회 성공"),
          @ApiResponse(responseCode = "404", description = "binaryContentId에 해당하는 BinaryContnet가 존재하지 않음")
      })
  @GetMapping
  public ResponseEntity<List<BinaryContentDTO>> getBinaryContentAllIn(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
    List<BinaryContentDTO> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity.ok(binaryContents);
  }
}
