package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.Api.BinaryContentApi;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@ResponseBody
//@RequestMapping("/api/binaryContent")
public class BinaryContentController implements BinaryContentApi {

  private final BinaryContentService binaryContentService;
  private final BinaryContentMapper binaryContentMapper;

  @Override
  public ResponseEntity<BinaryContentDto> find(UUID binaryContentId) {
    UUID uuid = UUID.fromString(binaryContentId.toString());
    BinaryContent binaryContent = binaryContentService.find(uuid);

    BinaryContentDto dto = binaryContentMapper.toDto(binaryContent);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(dto);
  }

  @Override
  public ResponseEntity<Object> findAllByIdIn(Object binaryContentIds) {
    List<UUID> uuids = new ArrayList<>();
    List<BinaryContent> binaryContents = new ArrayList<>();
    try {
      if (binaryContentIds instanceof ArrayList) {
        uuids = (ArrayList<UUID>) binaryContentIds;
        binaryContents = binaryContentService.findAllByIdIn(uuids);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(binaryContents);
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
