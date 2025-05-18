package com.sprint.mission.discodeit.domain.binarycontent.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import static com.sprint.mission.discodeit.util.FileUtils.getBytesFromMultiPartFile;

public record BinaryContentRequest(
        @NotBlank String fileName,
        @NotBlank String contentType,
        @NotBlank long size,
        byte[] bytes) {

    public static BinaryContentRequest fromMultipartFile(MultipartFile multipartFile) {
        return new BinaryContentRequest(
                multipartFile.getName(),
                multipartFile.getContentType(),
                multipartFile.getSize(),
                getBytesFromMultiPartFile(multipartFile)
        );
    }

}
