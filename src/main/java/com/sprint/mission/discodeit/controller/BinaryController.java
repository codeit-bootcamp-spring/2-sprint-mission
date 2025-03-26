package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryController {
    private final BinaryContentService binaryContentService;

    @GetMapping("/find/{binaryContentId}")
    public ResponseEntity<Map<String,String>> findBinaryContent(@PathVariable UUID binaryContentId) {
        BinaryContent content = binaryContentService.findById(binaryContentId);

        if (content == null) {
            return ResponseEntity.notFound().build();
        }

        String base64 = Base64.getEncoder().encodeToString(content.getBytes());

        Map<String, String> response = new HashMap<>();
        response.put("contentType", content.getContentType());
        response.put("bytes", base64);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/find/more")
    public ResponseEntity<List<Map<String,String>>> findBinaryContents(@RequestParam List<UUID> binaryContentIds) {
        Map<String, String> tempResponse = new HashMap<>();
        List<Map<String, String>> response = new ArrayList<>();

        for (UUID binaryContentId : binaryContentIds) {
            BinaryContent content = binaryContentService.findById(binaryContentId);
            tempResponse.put("contentType", content.getContentType());
            String base64 = Base64.getEncoder().encodeToString(content.getBytes());
            tempResponse.put("bytes", base64);
            response.add(tempResponse);
        }

        return ResponseEntity.ok(response);
    }
}
