package com.sprint.mission.discodeit.Controller;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
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
    public ResponseEntity<BinaryContent> find(@RequestParam("binaryContentId") UUID binaryContentId) {
        return ResponseEntity.ok(binaryContentService.find(binaryContentId));
    }

    @GetMapping("/findall")
    public ResponseEntity<List<BinaryContent>> findAll(@RequestParam("id") List<UUID> binaryContentId) {
        List<BinaryContent> allbinary = binaryContentService.findAllByIdIn(binaryContentId);
        return ResponseEntity.ok(allbinary);
    }

}
