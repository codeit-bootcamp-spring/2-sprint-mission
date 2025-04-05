package com.sprint.mission.discodeit.util;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

  public static <T> T saveToFile(Path directory, T object, UUID objectId) {
    Path filePath = getFilePath(directory, objectId);
    try (FileOutputStream fos = new FileOutputStream(filePath.toFile());
        ObjectOutputStream oos = new ObjectOutputStream(fos)) {
      oos.writeObject(object);
    } catch (IOException e) {
      throw new RuntimeException("파일 저장 실패: " + filePath, e);
    }
    return object;
  }

  public static <T> Optional<T> loadFromFile(Path directory, UUID objectId) {
    Path filePath = getFilePath(directory, objectId);
    if (Files.exists(filePath)) {
      try (FileInputStream fis = new FileInputStream(filePath.toFile());
          ObjectInputStream ois = new ObjectInputStream(fis)) {
        T object = (T) ois.readObject();
        return Optional.ofNullable(object);
      } catch (IOException | ClassNotFoundException e) {
        throw new RuntimeException("파일 로드 실패: " + filePath, e);
      }
    }
    return Optional.empty();
  }

  public static <T> List<T> loadAllFiles(Path directory, String extension) {
    List<T> objectList = new ArrayList<>();
    try {
      Files.list(directory)
          .filter(path -> path.toString().endsWith(extension))
          .forEach(path -> {
            UUID objectId = UUID.fromString(path.getFileName().toString().replace(".ser", ""));
            Optional<T> objectOpt = loadFromFile(directory, objectId);
            objectOpt.ifPresent(objectList::add);
          });
    } catch (IOException e) {
      throw new RuntimeException("디렉토리 로드 실패: " + directory, e);
    }
    return objectList;
  }

  public static void deleteFile(Path directory, UUID objectId) {
    try {
      Path filePath = getFilePath(directory, objectId);
      Files.deleteIfExists(filePath);
    } catch (IOException e) {
      throw new RuntimeException("파일 삭제 실패: " + objectId, e);
    }
  }

  public static Path getFilePath(Path directory, UUID id) {
    return directory.resolve(id.toString() + ".ser");
  }

  public static void init(Path directory) {
    try {
      if (!Files.exists(directory)) {
        Files.createDirectories(directory);
      }
    } catch (IOException e) {
      throw new RuntimeException("디렉토리 생성 실패: " + directory, e);
    }
  }


}
