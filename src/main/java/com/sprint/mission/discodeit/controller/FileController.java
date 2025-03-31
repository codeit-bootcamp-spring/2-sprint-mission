package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.AttachmentDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/files")
@RequiredArgsConstructor
public class FileController {
    private final BinaryContentService binaryContentService;

    // GET 바이너리 파일 조회
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<AttachmentDTO>> getFile(
            // ids가 없으면 전체를 조회하는 것이고, 있으면 해당 id에 해당하는 파일을 조회
            @RequestParam(value = "ids", required = false) List<UUID> ids) {
         List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(ids);
         List<AttachmentDTO> files = new ArrayList<>();
         for (BinaryContent binaryContent : binaryContents) {
             files.add(new AttachmentDTO(
                     binaryContent.getContentType(),
                     binaryContent.getContent()
             ));
         }
         return ResponseEntity.ok(files);
    }
}
