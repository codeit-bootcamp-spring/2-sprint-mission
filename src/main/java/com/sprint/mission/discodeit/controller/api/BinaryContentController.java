package com.sprint.mission.discodeit.controller.api;


import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentType;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.GET)
    public ResponseEntity<?> findById(@PathVariable("binaryContentId") UUID binaryContentId, @RequestBody BinaryContentDTO request) {
       BinaryContent binaryContent = binaryContentService.find(binaryContentId);
       BinaryContentDTO dto = new BinaryContentDTO(binaryContent.getType(), binaryContent.getContent());

       return ResponseEntity.ok(dto);
    }
}
