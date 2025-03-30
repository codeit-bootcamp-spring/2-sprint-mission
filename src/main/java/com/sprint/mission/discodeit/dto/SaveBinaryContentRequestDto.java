package com.sprint.mission.discodeit.dto;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public record SaveBinaryContentRequestDto(
        String fileName,
        String contentType,
        byte[] fileData
) {
    public static SaveBinaryContentRequestDto from(MultipartFile file) throws IOException {
        return new SaveBinaryContentRequestDto(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
        );
    }

    public static Optional<SaveBinaryContentRequestDto> nullableFrom(MultipartFile file) throws IOException {
        return Optional.ofNullable(file)
                .filter(f -> !f.isEmpty())
                .map(f -> {
                    try {
                        return new SaveBinaryContentRequestDto(
                                f.getOriginalFilename(),
                                f.getContentType(),
                                f.getBytes()
                        );
                    } catch (IOException e) {
                        throw new RuntimeException("파일 변환 중 오류 발생 : " + e);
                    }
                });
    }
}
