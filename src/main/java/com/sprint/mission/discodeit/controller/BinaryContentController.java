package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.dto.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;
import org.springframework.web.context.request.NativeWebRequest;

@RequiredArgsConstructor
@Controller
@ResponseBody
@RequestMapping("/api/binaryContent")
public class BinaryContentController implements BinaryContentApi {

  private final BinaryContentService binaryContentService;

  @Override
  public ResponseEntity<BinaryContent> find(UUID binaryContentId) {
    binaryContentService.find(binaryContentId);
    return BinaryContentApi.super.find(binaryContentId);
  }

  @Override
  public ResponseEntity<List<BinaryContent>> findAllByIdIn(List<UUID> binaryContentIds) {
    binaryContentService.findAllByIdIn(binaryContentIds);
    return BinaryContentApi.super.findAllByIdIn(binaryContentIds);
  }
  /*@RequestMapping(path = "find")
  public ResponseEntity<BinaryContent> find(@RequestParam("binaryContentId") UUID binaryContentId) {
    BinaryContent binaryContent = binaryContentService.find(binaryContentId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(binaryContent);
  }

  @RequestMapping(path = "findAllByIdIn")
  public ResponseEntity<List<BinaryContent>> findAllByIdIn(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
    List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(binaryContents);
  }*/
}
