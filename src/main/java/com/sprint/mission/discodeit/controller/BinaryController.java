package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryController {
    private final BinaryContentService binaryContentService;

    @GetMapping("/find")
    public ResponseEntity<Map<String,String>> findBinaryContent(@RequestParam UUID binaryContentId) {
        BinaryContent content = binaryContentService.findById(binaryContentId);

        if (content == null) {
            return ResponseEntity.notFound().build();
        }

        String base64 = Base64.getEncoder().encodeToString(content.getBytes());

        Map<String, String> response = new HashMap<>();
        response.put("contentType", content.getContentType());
        response.put("bytes", base64);

        return ResponseEntity.ok(response);
//        return ResponseEntity.ok()
//                .contentType(MediaType.valueOf(content.getContentType()))
//                .body(content.getBytes());
    }
}
