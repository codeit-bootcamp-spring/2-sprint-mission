package com.sprint.mission.discodeit.dto.binarycontent;

import com.sprint.mission.discodeit.exception.binarycontent.FileLoadException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public record BinaryContentCreateRequest(
    @NotBlank(message = "파일 이름은 필수입니다.")
    String fileName,

    @NotBlank(message = "컨텐츠 타입은 필수입니다.")
    String contentType,

    @NotNull(message = "파일 데이터는 필수입니다.")
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
