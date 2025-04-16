package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;


  @RequestMapping(value = "", method = RequestMethod.GET)
  public ResponseEntity<List<BinaryContentDto>> getBinaryContents(
      @RequestParam("binaryContentIds") List<UUID> binaryIds) {
    List<BinaryContentDto> binaryContents = binaryIds.stream()
        .map(binaryContentService::findBinaryContent)
        .toList();

    return ResponseEntity.ok(binaryContents);
  }

  @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.GET)
  public ResponseEntity<BinaryContentDto> getBinaryContent(
      @PathVariable("binaryContentId") UUID binaryContentId) {
    return ResponseEntity.ok(binaryContentService.findBinaryContent(binaryContentId));
  }

//  @RequestMapping(value = "/upload", method = RequestMethod.PUT)
//  public ResponseEntity<BinaryContentDto> upload(
//      @RequestParam("file") MultipartFile multipartFile) {
//    BinaryContentDto binaryContent = binaryContentService.createBinaryContent(multipartFile);
//
//    return ResponseEntity.ok(binaryContent);
//  }

  @GetMapping(value = "/{binaryContentId}/download")
  public ResponseEntity<?> downloadBinaryContent(
      @PathVariable("binaryContentId") UUID binaryContentId
  ) {
    return binaryContentStorage.download(binaryContentService.findBinaryContent(binaryContentId));
  }
}
