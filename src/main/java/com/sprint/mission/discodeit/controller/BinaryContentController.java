package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;


    @GetMapping("/find")
    public ResponseEntity<BinaryContent> findBinaryContent(@RequestParam UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentService.find(binaryContentId);
        return ResponseEntity.ok(binaryContent);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<BinaryContent>> findAllBinaryContents(@RequestParam List<UUID> binaryContentIds) {
        List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
        return ResponseEntity.ok(binaryContents);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewFile(@PathVariable UUID id) {
        BinaryContent binaryContent = binaryContentService.find(id);

        return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(binaryContent.getContentType()))
                .body(binaryContent.getBytes());
    }
}
