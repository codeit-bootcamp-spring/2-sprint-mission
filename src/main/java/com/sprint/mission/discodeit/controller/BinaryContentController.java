package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContents")
@Tag(name = "BinaryContent", description = "첨부 파일 API")
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @PostMapping("/create")
  public ResponseEntity<BinaryContent> createBinaryContent(
      @RequestParam("fileName") String fileName,
      @RequestParam("contentType") String contentType,
      @RequestParam("file") byte[] fileBytes) {

    BinaryContent binaryContent = binaryContentService.create(
        new BinaryContentCreateRequest(fileName, contentType, fileBytes));

    return new ResponseEntity<>(binaryContent, HttpStatus.CREATED);
  }

  @GetMapping("/find/{binaryContentId}")
  public ResponseEntity<BinaryContent> findBinaryContent(
      @RequestParam UUID binaryContentId) {

    BinaryContent binaryContent = binaryContentService.find(binaryContentId);

    return new ResponseEntity<>(binaryContent, HttpStatus.OK);
  }

  @GetMapping("/findAll")
  public ResponseEntity<List<BinaryContent>> findMultipleBinaryContent(
      @RequestParam List<UUID> binaryContentIds) {
    List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
    return new ResponseEntity<>(binaryContents, HttpStatus.OK);
  }

  @DeleteMapping("/delete/{binaryContentId}")
  public ResponseEntity<Void> deleteBinaryContent(
      @PathVariable UUID binaryContentId) {

    binaryContentService.delete(binaryContentId);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
