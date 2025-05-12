package com.sprint.mission.discodeit.dto.binarycontent;

import com.sprint.mission.discodeit.exception.binarycontent.FileLoadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public record BinaryContentCreateRequest(
    String fileName,
    String contentType,
    byte[] bytes
) {

  public static BinaryContentCreateRequest fromMultipartFile(MultipartFile file) {
    try {
      return new BinaryContentCreateRequest(
          file.getOriginalFilename(),
          file.getContentType(),
          file.getBytes()
      );
    } catch (IOException e) {
      throw new FileLoadException();
    }

  }
}
