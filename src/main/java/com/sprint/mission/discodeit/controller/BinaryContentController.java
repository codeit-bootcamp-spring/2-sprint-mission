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
@RequestMapping("/api/binaryContent")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @GetMapping("/download")
    public ResponseEntity<List<BinaryContent>> downloadBinaryList(@RequestParam List<UUID> binaryIds) {
        System.out.println("바이너리 여러 파일 조회 API 실행");
        List<BinaryContent> binaryContentList = binaryContentService.findAllByIdIn(binaryIds);
        System.out.println("binary list :" + binaryContentList);
        return ResponseEntity.ok(binaryContentList);
    }

    @GetMapping("/find")
    public ResponseEntity<BinaryContent> findBinary(@RequestParam UUID binaryContentId) {
        System.out.println("바이너리 단일(프로필) 파일 조회 API 실행");
        BinaryContent binaryContent = binaryContentService.find(binaryContentId);
        System.out.println("binary Content(profile) :" + binaryContent);
        return ResponseEntity.ok(binaryContent);
    }
}
