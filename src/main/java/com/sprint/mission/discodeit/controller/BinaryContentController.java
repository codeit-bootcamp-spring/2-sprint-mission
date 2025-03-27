package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/binary-contents")
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> find(@PathVariable UUID id) {
        BinaryContent content = binaryContentService.find(id);
        if (content == null) {
            throw new NoSuchElementException("BinaryContent with id" + id + "not found");
        }
        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=" + content.getFileName())
                .header("Content-Type", content.getContentType())
                .body(content.getBytes());
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContent>> findAll(@RequestParam List<UUID> ids){
        List<BinaryContent> contents = binaryContentService.findAllByIdIn(ids);
        return ResponseEntity.ok(contents);
    }
}
