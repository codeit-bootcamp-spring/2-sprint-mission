package com.sprint.mission.discodeit.core.storage.dto;

import java.io.IOException;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record BinaryContentCreateRequest(
    String fileName,
    String contentType,
    byte[] bytes
) {

  public static BinaryContentCreateRequest create(MultipartFile file) throws IOException {
    return BinaryContentCreateRequest.builder()
        .fileName(file.getOriginalFilename())
        .contentType(file.getContentType())
        .bytes(file.getBytes())
        .build();
  }
}
