package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService binaryContentService;

  @RequestMapping(value = "/upload", method = RequestMethod.PUT)
  public ResponseEntity<?> upload(@RequestParam("file") MultipartFile multipartFile)
      throws IOException {

    CreateBinaryContentRequest request = CreateBinaryContentRequest.builder()
        .fileName(multipartFile.getOriginalFilename())
        .size(multipartFile.getSize())
        .contentType(multipartFile.getContentType())
        .bytes(multipartFile.getBytes())
        .build();

    UUID binaryId = binaryContentService.createBinaryContent(request);

    return ResponseEntity.ok(binaryId);
  }

  @RequestMapping(value = "{binaryContentId}", method = RequestMethod.GET)
  public ResponseEntity<?> getBinaryContent(@PathVariable UUID binaryContentId) {
    return ResponseEntity.ok(binaryContentService.findBinaryContent(binaryContentId));
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  public ResponseEntity<?> getBinaryContents(@RequestParam List<UUID> binaryIds) {
    List<BinaryContent> binaryContents = binaryIds.stream()
        .map(binaryContentService::findBinaryContent)
        .toList();

    return ResponseEntity.ok(binaryContents);
  }
}
