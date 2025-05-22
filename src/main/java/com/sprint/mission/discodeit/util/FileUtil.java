package com.sprint.mission.discodeit.util;

import com.sprint.mission.discodeit.exception.binarycontent.FileAlreadyExistsException;
import com.sprint.mission.discodeit.exception.binarycontent.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileUtil {

  public static void init(Path directory) {
    if (!Files.exists(directory)) {
      try {
        Files.createDirectories(directory);
      } catch (IOException e) {
        log.error("폴더 생성 중 오류 발생", e);
        throw new RuntimeException(e);
      }
    }
  }

  public static Path resolvePath(Path directory, UUID id) {
    return directory.resolve(id.toString());
  }

  public static UUID save(Path root, UUID id, byte[] data) {
    Path filePath = resolvePath(root, id);
    if (Files.exists(filePath)) {
      throw new FileAlreadyExistsException().duplicateFile(filePath.toString());
    }
    try (OutputStream fos = Files.newOutputStream(filePath)) {
      fos.write(data);
    } catch (IOException e) {
      log.error("파일 저장 중 오류 발생: filePath={}", filePath, e);
      throw new RuntimeException(e);
    }
    return id;
  }

  public static InputStream get(Path root, UUID id) {
    Path filePath = resolvePath(root, id);
    if (!Files.exists(filePath)) {
      throw new FileNotFoundException().notFoundFile(filePath.toString());
    }
    try {
      return Files.newInputStream(filePath);
    } catch (IOException e) {
      log.error("파일 읽기 중 오류 발생: filePath={}", filePath, e);
      throw new RuntimeException(e);
    }
  }


  public static void delete(Path directory, UUID id) {
    Path filePath = resolvePath(directory, id);
    try {
      Files.delete(filePath);
    } catch (IOException e) {
      log.error("파일 삭제 중 오류 발생: filePath={}", filePath, e);
      throw new RuntimeException(e);
    }
  }

  public static boolean isAllowedExtension(String originalFilename) {
    int dotIndex = originalFilename.lastIndexOf('.');
    if (dotIndex == -1) {
      return false;  // 확장자가 없다면 false
    }
    String ext = originalFilename.substring(dotIndex).toLowerCase();
    return FileExtensionUtil.ALLOWED_EXTENSIONS.contains(ext);
  }
}