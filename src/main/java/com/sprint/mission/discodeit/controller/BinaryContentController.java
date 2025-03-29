package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.controller.binarycontent.BinaryContentListDTO;
import com.sprint.mission.discodeit.dto.controller.binarycontent.FindAllBinaryContentInRequestDTO;
import com.sprint.mission.discodeit.dto.controller.binarycontent.FindBinaryContentResponseDTO;
import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDTO;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/binarycontents")
public class BinaryContentController {
    private final BinaryContentService binaryContentService;
    private final BinaryContentMapper binaryContentMapper;

    @GetMapping("/{binaryContentId}")
    public ResponseEntity<FindBinaryContentResponseDTO> getBinaryContent(@PathVariable("binaryContentId")UUID id) {
        BinaryContentDTO binaryContentDTO = binaryContentService.find(id);
        return ResponseEntity.ok(binaryContentMapper.toBinaryContentResponseDTO(binaryContentDTO));
    }

    @GetMapping
    // GetMapping엔 @RequestBody를 사용하지 못했었지만, 요새는 사용하는 추세
    public ResponseEntity<BinaryContentListDTO> getBinaryContentAllIn(@RequestBody @Valid FindAllBinaryContentInRequestDTO findAllBinaryContentInRequestDTO) {
        List<BinaryContentDTO> binaryContentDTOList = binaryContentService.findAllByIdIn(findAllBinaryContentInRequestDTO.attachmentIds());
        return ResponseEntity.ok(new BinaryContentListDTO(binaryContentDTOList));
    }
}
