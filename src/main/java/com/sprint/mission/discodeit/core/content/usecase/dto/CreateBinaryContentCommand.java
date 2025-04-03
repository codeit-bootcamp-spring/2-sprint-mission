package com.sprint.mission.discodeit.core.content.usecase.dto;

import java.io.IOException;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record CreateBinaryContentCommand(
    String fileName,
    String contentType,
    byte[] bytes
) {

  public static CreateBinaryContentCommand create(MultipartFile file) throws IOException {
    return CreateBinaryContentCommand.builder()
        .fileName(file.getName())
        .contentType(file.getContentType())
        .bytes(file.getBytes())
        .build();
  }
}
