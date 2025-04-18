package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.BinaryContentApi;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentApi {

    private final BinaryContentService binaryContentService;

    @Override
    @GetMapping("/{binaryContentId}")
    public ResponseEntity<BinaryContentDto> findBinaryContent(@PathVariable UUID binaryContentId) {
        BinaryContentDto binaryContent = binaryContentService.find(binaryContentId);
        return ResponseEntity.status(HttpStatus.OK).body(binaryContent);
    }
    @Override
    @GetMapping
    public ResponseEntity<List<BinaryContentDto>> findAllBinaryContentByIds(@RequestParam List<UUID> binaryContentIds) {
        List<BinaryContentDto> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
        return ResponseEntity.ok(binaryContents);
    }

}
