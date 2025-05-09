package com.sprint.mission.discodeit.dto.request.binarycontent;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

import static com.sprint.mission.discodeit.util.FileUtils.getBytesFromMultiPartFile;

@Schema(description = "바이너리 파일 생성 요청")
public record BinaryContentRequest(
        @NotBlank String fileName,
        @NotBlank String contentType,
        byte[] bytes) {
    public static BinaryContentRequest fromMultipartFile(MultipartFile multipartFile) {
        return new BinaryContentRequest(multipartFile.getName(), multipartFile.getContentType(), getBytesFromMultiPartFile(multipartFile));
    }
}
