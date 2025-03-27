package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.BinaryContentFindDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @GetMapping("/find")
    public ResponseEntity<BinaryContent> find(
            @RequestParam UUID id
    ) {
        BinaryContent binaryContent = binaryContentService.find(id);
        return ResponseEntity.ok(binaryContent);
    }


    @GetMapping("/findAll")
    public ResponseEntity<List<BinaryContent>> findAll() {
        List<BinaryContent> binaryContents = binaryContentService.findAll();
        return ResponseEntity.ok(binaryContents);
    }
}
