package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binary")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @GetMapping("/download")
    public ResponseEntity<List<BinaryContent>> downloadBinaryList(@RequestParam List<UUID> binaryIds) {
        System.out.println("바이너리 파일 조회 API 실행");
        List<BinaryContent> binaryContentList = binaryContentService.findAllByIdIn(binaryIds);
        System.out.println("binary list :" + binaryContentList);
        return ResponseEntity.ok(binaryContentList);
    }
}
