package com.sprint.mission.discodeit.dto;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public record SaveBinaryContentParamDto(
        String fileName,
        String contentType,
        byte[] fileData
) {
    public static SaveBinaryContentParamDto from(MultipartFile file) throws IOException {
        return new SaveBinaryContentParamDto(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
        );
    }
}
