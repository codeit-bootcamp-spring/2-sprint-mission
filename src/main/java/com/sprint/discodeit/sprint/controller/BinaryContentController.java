package com.sprint.discodeit.sprint.controller;

import com.sprint.discodeit.sprint.domain.entity.BinaryContent;
import com.sprint.discodeit.sprint.service.basic.util.BinaryContentService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @GetMapping("/find")
    public ResponseEntity<BinaryContent> findBinaryContent(@RequestParam String binaryContentId) {
        BinaryContent binaryContent = binaryContentService.find(UUID.fromString(binaryContentId));
        return ResponseEntity.ok(binaryContent);
    }
}
