package com.sprint.mission.discodeit.dto.binarycontent;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public record BinaryContentCreateRequest(
        String fileName,
        String contentType,
        byte[] bytes
) {
    public static BinaryContentCreateRequest fromMultipartFile(MultipartFile file) throws IOException {
        return new BinaryContentCreateRequest(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
        );
    }
}
