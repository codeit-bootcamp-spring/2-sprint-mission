package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.dto.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.ArrayList;
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
  public ResponseEntity<BinaryContent> find(Object binaryContentId) {
    UUID uuid = UUID.fromString(binaryContentId.toString());
    binaryContentService.find(uuid);
    return BinaryContentApi.super.find(uuid);
  }

  @Override
  public ResponseEntity<Object> findAllByIdIn(Object binaryContentIds) {
    List<UUID> uuids = new ArrayList<>();
    try{
      if(binaryContentIds instanceof ArrayList) {
        uuids = (ArrayList<UUID>) binaryContentIds;
        binaryContentService.findAllByIdIn(uuids);
      }
    }catch (Exception e) {
      throw new RuntimeException(e);
    }
    return BinaryContentApi.super.findAllByIdIn(uuids);
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
