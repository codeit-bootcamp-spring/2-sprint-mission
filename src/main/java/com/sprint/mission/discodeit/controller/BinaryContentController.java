package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.DTO.BinaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binary-content")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    //바이너리 파일 1개 조회
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<BinaryContentDto> getBinaryContent(@PathVariable UUID id){
        BinaryContentDto binaryContent = binaryContentService.find(id);
        return ResponseEntity.ok(binaryContent);
    }

    //바이너리 파일 여러 개 조회
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContentDto>> getBinaryContent(@RequestParam List<UUID> ids){
        List<BinaryContentDto> binaryContents = binaryContentService.findAllByIdIn(ids);
        return ResponseEntity.ok(binaryContents);
    }

}
