package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.swagger.BinaryContentApi;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentApi {

    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;

    @GetMapping("/{binaryContentId}")
    @Override
    public ResponseEntity<BinaryContentDto> find(
        @PathVariable("binaryContentId") UUID binaryContentId
    ) {
        BinaryContentDto binaryContentDto = binaryContentService.findById(binaryContentId);
        return ResponseEntity.status(HttpStatus.OK).body(binaryContentDto);
    }

    @GetMapping("")
    @Override
    public ResponseEntity<List<BinaryContentDto>> findByIdIn(
        @RequestParam("binaryContentIds") List<UUID> binaryContentIdList
    ) {
        List<BinaryContentDto> binaryContentDtoList = binaryContentService.findByIdIn(
            binaryContentIdList);
        return ResponseEntity.status(HttpStatus.OK).body(binaryContentDtoList);
    }

    @GetMapping("/{binaryContentId}/download")
    @Override
    public ResponseEntity<Resource> binaryContentDownload(
        @PathVariable("binaryContentId") UUID binaryContentId
    ) {
        log.info("파일 다운로드 진행: binaryContentId = {}", binaryContentId);
        BinaryContentDto binaryContentDto = binaryContentService.findById(binaryContentId);
        log.info("파일 다운로드 완료: binaryContentsId = {}", binaryContentDto.id());
        return binaryContentStorage.download(binaryContentDto);
    }
}
