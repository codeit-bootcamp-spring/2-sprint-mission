package com.sprint.mission.discodeit.util;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Component
public class FileUtil {

  public static Optional<BinaryContentCreateRequest> toBinaryRequest(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      return Optional.empty();
    }
    try {
      return Optional.of(new BinaryContentCreateRequest(
          file.getOriginalFilename(),
          file.getContentType(),
          file.getBytes()
      ));
    } catch (IOException e) {
      throw new RuntimeException("MultipartFile 변환 실패",
          e);
    }
  }

}