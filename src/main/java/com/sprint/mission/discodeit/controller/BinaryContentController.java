package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/binaryContent")
public class BinaryContentController {

    private final BinaryContentService binaryContentService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity<BinaryContent> createBinaryContent(
            @RequestParam("fileName") String fileName,
            @RequestParam("contentType") String contentType,
            @RequestParam("file") byte[] fileBytes) {

        BinaryContent binaryContent = binaryContentService.create(new BinaryContentCreateRequest(fileName, contentType, fileBytes));

        return new ResponseEntity<>(binaryContent, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ResponseEntity<BinaryContent> findBinaryContent(
            @RequestParam UUID binaryContentId) {

        BinaryContent binaryContent = binaryContentService.find(binaryContentId);

        return new ResponseEntity<>(binaryContent, HttpStatus.OK);
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<BinaryContent>> findMultipleBinaryContent(
            @RequestParam List<UUID> binaryContentIds) {
        List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
        return new ResponseEntity<>(binaryContents, HttpStatus.OK);
    }

    @RequestMapping(value = "/delete/{binaryContentId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteBinaryContent(
            @PathVariable UUID binaryContentId) {

        binaryContentService.delete(binaryContentId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
