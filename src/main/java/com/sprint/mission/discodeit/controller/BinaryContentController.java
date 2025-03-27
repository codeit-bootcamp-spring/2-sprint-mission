package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @RequestMapping(value = "/aa", method = RequestMethod.GET)
    public ResponseEntity<?> getBinaryContent(@RequestBody List<UUID> binaryIds) {
        List<BinaryContent> binaryContents = binaryIds.stream()
                .map(binaryContentService::findBinaryContent)
                .toList();

        return ResponseEntity.ok(binaryContents);
    }
}
