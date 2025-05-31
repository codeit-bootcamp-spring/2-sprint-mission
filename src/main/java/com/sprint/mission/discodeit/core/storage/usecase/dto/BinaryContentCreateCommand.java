package com.sprint.mission.discodeit.core.storage.usecase.dto;

import java.io.IOException;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record BinaryContentCreateCommand(
    String fileName,
    String contentType,
    byte[] bytes
) {

  public static BinaryContentCreateCommand create(MultipartFile file) throws IOException {
    return BinaryContentCreateCommand.builder()
        .fileName(file.getOriginalFilename())
        .contentType(file.getContentType())
        .bytes(file.getBytes())
        .build();
  }
}
