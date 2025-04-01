package com.sprint.mission.discodeit.controller.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.basic.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BasicBinaryContentController {

    private final BinaryContentService binaryContentService;
    Logger logger = LoggerFactory.getLogger(BasicBinaryContentController.class);

    @GetMapping("/find")
    public ResponseEntity<BinaryContent> find(@RequestParam String binaryContentId) {
        BinaryContent response = binaryContentService.find(UUID.fromString(binaryContentId));
        logger.info("Successfully found binaryContent with id {}", binaryContentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/findByIdIn")
    public ResponseEntity<List<BinaryContent>> findBinaryContentByIdIn(@RequestBody List<UUID> binaryContentIds) {
        List<BinaryContent> response = binaryContentService.findAllByIdIn(binaryContentIds);
        logger.info("Successfully found binaryContents with ids {}", binaryContentIds);
        return ResponseEntity.ok(response);
    }

}
