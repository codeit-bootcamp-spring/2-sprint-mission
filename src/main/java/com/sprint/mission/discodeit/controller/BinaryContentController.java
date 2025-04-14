package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentListResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
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
  public ResponseEntity<BinaryContent> upload(@RequestParam("file") MultipartFile multipartFile) {
    BinaryContent binaryId = binaryContentService.createBinaryContent(multipartFile);

    return ResponseEntity.ok(binaryId);
  }

  @RequestMapping(value = "{binaryContentId}", method = RequestMethod.GET)
  public ResponseEntity<BinaryContent> getBinaryContent(
      @PathVariable("binaryContentId") UUID binaryContentId) {
    return ResponseEntity.ok(binaryContentService.findBinaryContent(binaryContentId));
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  public ResponseEntity<BinaryContentListResponse> getBinaryContents(
      @RequestParam("binaryContentIds") List<UUID> binaryIds) {
    List<BinaryContent> binaryContents = binaryIds.stream()
        .map(binaryContentService::findBinaryContent)
        .toList();

    return ResponseEntity.ok(new BinaryContentListResponse(binaryContents));
  }
}
