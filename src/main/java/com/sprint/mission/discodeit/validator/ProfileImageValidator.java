package com.sprint.mission.discodeit.validator;

import com.sprint.mission.discodeit.exception.file.ProfileFileTypeException;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
public class ProfileImageValidator {

  private static final Tika tika = new Tika();
  private static final Set<String> ALLOWED_MIME_TYPES = Set.of("image/jpeg", "image/png",
      "image/gif", "image/webp");
  private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp");

  public static void validateProfileImageType(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      return;
    }
    // 1. MIME 타입 검증 (Tika로 실제 파일 내용 기반 검사)
    try {
      String mimeType = tika.detect(file.getInputStream());
      if (!ALLOWED_MIME_TYPES.contains(mimeType)) {
        throw new ProfileFileTypeException(Map.of("mimeType", mimeType));
      }
    } catch (IOException e) {
      throw new ProfileFileTypeException();
    }

    // 2. 확장자 검증 (이중 체크)
    String extension = FilenameUtils.getExtension(file.getOriginalFilename());
    if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
      throw new ProfileFileTypeException(Map.of("extension", extension));
    }
  }

}
