package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/binary")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> downloadSingleFile(@PathVariable UUID fileId) {
        byte[] fileData = binaryContentService.getById(fileId).bytes();

        ByteArrayResource resource = new ByteArrayResource(fileData);

        return ResponseEntity.ok()
                .body(resource);
    }
}
