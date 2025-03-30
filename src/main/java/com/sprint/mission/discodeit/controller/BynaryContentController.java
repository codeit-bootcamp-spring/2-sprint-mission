package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binary-content")
public class BynaryContentController {

    private final BinaryContentService binaryContentService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<BinaryContent> getBinaryContentById(@PathVariable("id") UUID binaryContentId) {
        return ResponseEntity.ok(binaryContentService.findById(binaryContentId));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContent>> getBinaryContents(@RequestParam List<UUID> binaryContentIds) {
        System.out.println(binaryContentIds);
        return ResponseEntity.ok(binaryContentService.findAllByIdIn(binaryContentIds));
    }
}
