package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController

@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ResponseEntity<?> findBinaryContent(@RequestParam("binaryContentId") List<UUID> binaryContentIds) {
        if (binaryContentIds.size() == 1) {
            BinaryContent binaryContent = binaryContentService.find(binaryContentIds.get(0));
            return ResponseEntity.ok(binaryContent);
        } else {
            List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
            return ResponseEntity.ok(binaryContents);
        }
    }
}
