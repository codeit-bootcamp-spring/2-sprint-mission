package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.stereotype.Component;

@Component
public class BinaryContentMapper {

  public BinaryContentDto toDto(BinaryContent binaryContent, InputStream is) {
    try {
      byte[] bytes = is.readAllBytes();
      is.close();
      return new BinaryContentDto(
          binaryContent.getId(),
          binaryContent.getFileName(),
          binaryContent.getSize(),
          binaryContent.getContentType(),
          bytes
      );
    } catch (IOException e) {
      System.err.println(e.getMessage());
      throw new RuntimeException("파일 변환 중 오류 발생 : ", e);
    }
  }
}
