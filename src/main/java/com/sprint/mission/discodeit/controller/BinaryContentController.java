package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.dto.BinaryContentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @GetMapping("/find")
    public ResponseEntity<Map<String, String>> find(@RequestParam UUID binaryContentId) {
        byte[] file = binaryContentService.findBinaryById(binaryContentId);

        // 파일을 Base64로 인코딩
        String base64Image = Base64.getEncoder().encodeToString(file);
        String contentType = "image/jpeg";

        // JSON 응답으로 반환
        Map<String, String> response = new HashMap<>();
        response.put("contentType", contentType);
        response.put("bytes", base64Image);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BinaryContentResponseDto>> getFiles(@RequestParam List<UUID> ids) {
        List<BinaryContentResponseDto> files = binaryContentService.findAllByIdIn(ids);
        return ResponseEntity.ok(files);
    }

}
