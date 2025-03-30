package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/binaryContent")
public class BinaryContentController {

    private final BinaryContentService binaryContentService;
    private static final Logger log = LoggerFactory.getLogger(BinaryContentController.class);

    @RequestMapping(value = "/read", method = RequestMethod.GET)
    public ResponseEntity<?> read(@RequestParam UUID binaryContentKey) {
        BinaryContent content = binaryContentService.find(binaryContentKey);
        log.info("단일 파일 조회 결과: {}", content);
        return ResponseEntity.ok(content);
    }

    @RequestMapping(value = "/readAll", method = RequestMethod.GET)
    public ResponseEntity<?> readAll(@RequestParam List<UUID> binaryContentKeys) {
        List<BinaryContent> contents = binaryContentService.findAllByKey(binaryContentKeys);
        log.info("다중 파일 조회 결과: {}", contents);
        return ResponseEntity.ok(contents);
    }
}
