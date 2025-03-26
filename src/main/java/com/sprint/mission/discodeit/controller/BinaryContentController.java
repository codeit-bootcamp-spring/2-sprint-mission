package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binary/")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public ResponseEntity<ByteArrayResource> get(@PathVariable UUID id) {
        BinaryContent content = binaryContentService.find(id);
        ByteArrayResource resource = new ByteArrayResource(content.getBytes());

        return ResponseEntity.ok()
                .contentLength(content.getSize())
                .contentType(MediaType.parseMediaType(content.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + content.getFileName() + "\"")
                .body(resource);
    }

    @RequestMapping(value = "/getMultiple", method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContent>> getMultiple(@RequestParam List<UUID> ids) {
        List<BinaryContent> contents = binaryContentService.findAllByIdIn(ids);
        return ResponseEntity.status(HttpStatus.OK).body(contents);
    }
}
