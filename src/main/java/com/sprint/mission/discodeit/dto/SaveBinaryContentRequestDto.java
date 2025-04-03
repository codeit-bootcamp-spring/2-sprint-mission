package com.sprint.mission.discodeit.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public record SaveBinaryContentRequestDto(
    String fileName,
    String contentType,
    byte[] fileData
) {

  public static Optional<SaveBinaryContentRequestDto> nullableFrom(MultipartFile file)
      throws IOException {
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

  public static List<SaveBinaryContentRequestDto> nullableFromList(List<MultipartFile> files)
      throws IOException {
    if (files == null || files.isEmpty()) {
      return List.of();
    }

    return files.stream()
        .filter(f -> !f.isEmpty())  // 빈 파일 제외
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
        })
        .toList();
  }

}
