package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Component
public class BinaryContentRequestHandler {
    public Optional<BinaryContentCreateRequest> handle(MultipartFile binaryContent) {
        if (binaryContent != null && !binaryContent.isEmpty()) {
            try {
                return Optional.of(new BinaryContentCreateRequest(
                        binaryContent.getOriginalFilename(),
                        binaryContent.getContentType(),
                        binaryContent.getBytes()
                ));
            } catch (IOException e) {
                throw new FileProcessingException("파일 처리 중 오류가 발생했습니다: " + e.getMessage());
            }
        }
        return Optional.empty();
    }
}
