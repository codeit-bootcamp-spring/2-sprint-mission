package com.sprint.mission.discodeit.domain.binarycontent.controller;

import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.domain.binarycontent.dto.BinaryContentResult;
import com.sprint.mission.discodeit.domain.binarycontent.service.BinaryContentService;
import com.sprint.mission.discodeit.domain.binarycontent.storage.BinaryContentStorage;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BinaryContentResult> create(@RequestPart MultipartFile multipartFile) throws MethodArgumentNotValidException {
        log.info("파일 업로드 요청: filename={}, size={}", multipartFile.getOriginalFilename(), multipartFile.getSize());
        BinaryContentRequest binaryContentRequest = BinaryContentRequest.fromMultipartFile(multipartFile);
        BinaryContentResult binaryContentResult = binaryContentService.createBinaryContent(binaryContentRequest);
        log.info("파일 업로드 성공: binaryContentId={}", binaryContentResult.id());

        return ResponseEntity.ok(binaryContentResult);
    }

    @GetMapping
    public ResponseEntity<List<BinaryContentResult>> getByIdIn(@RequestParam(value = "binaryContentIds") List<UUID> binaryContentIds) {
        log.debug("여러 파일 메타데이터 조회 요청: binaryContentIds={}", binaryContentIds);
        List<BinaryContentResult> binaryContentResults = binaryContentService.getByIdIn(binaryContentIds);
        log.info("여러 파일 메타데이터 조회 성공: 조회 수={}", binaryContentResults.size());

        return ResponseEntity.ok(binaryContentResults);
    }

    @GetMapping("/{binaryContentId}")
    public ResponseEntity<BinaryContentResult> getById(@PathVariable UUID binaryContentId) {
        log.debug("파일 메타데이터 단건 조회 요청: binaryContentId={}", binaryContentId);
        BinaryContentResult binaryContentResult = binaryContentService.getById(binaryContentId);
        log.info("파일 메타데이터 단건 조회 성공: binaryContentId={}", binaryContentId);

        return ResponseEntity.ok().body(binaryContentResult);
    }

    @GetMapping("{binaryContentId}/download")
    public ResponseEntity<?> download(@Valid @RequestBody BinaryContentResult binaryContentResult) {
        log.info("파일 다운로드 요청: binaryContentId={}", binaryContentResult.id());
        InputStreamResource download = binaryContentStorage.download(binaryContentResult);
        return ResponseEntity.ok(download);
    }

    @DeleteMapping("/{binaryContentId}")
    public ResponseEntity<Void> delete(@NotNull @PathVariable UUID binaryContentId) {
        log.warn("파일 삭제 요청: binaryContentId={}", binaryContentId);
        binaryContentService.delete(binaryContentId);
        log.info("파일 삭제 성공: binaryContentId={}", binaryContentId);

        return ResponseEntity.noContent().build();
    }

}
