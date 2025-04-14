package com.sprint.mission.discodeit.util;

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

public class FileUtil {

  private static final String EXTENSION = ".ser";

  public static void init(Path directory) {
    if (!Files.exists(directory)) {
      try {
        Files.createDirectories(directory);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static Path resolvePath(Path directory, UUID id) {
    return directory.resolve(id.toString() + EXTENSION);
  }

  public static <T> T save(Path directory, T object, UUID id) {
    Path filePath = resolvePath(directory, id);
    try (
        FileOutputStream fos = new FileOutputStream(filePath.toFile());
        ObjectOutputStream oos = new ObjectOutputStream(fos)
    ) {
      oos.writeObject(object);
    } catch (IOException e) {
      throw new RuntimeException("파일 저장 중 오류 발생");
    }
    return object;
  }

  public static <T> Optional<T> findById(Path directory, UUID id, Class<T> clazz) {
    Path filePath = resolvePath(directory, id);
    if (!Files.exists(filePath)) {
      return Optional.empty();
    }
    try (
        FileInputStream fis = new FileInputStream(filePath.toFile());
        ObjectInputStream ois = new ObjectInputStream(fis)
    ) {
      Object object = ois.readObject();
      if (clazz.isInstance(object)) {
        return Optional.of(clazz.cast(object));
      }
      return Optional.empty();
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException("파일 조회 중 오류 발생");
    }
  }

  public static <T> List<T> findAll(Path directory, Class<T> clazz) {
    List<T> objectList = new ArrayList<>();
    try {
      Files.list(directory)
          .filter(path -> path.toString().endsWith(EXTENSION))
          .forEach(path -> {
            UUID id = UUID.fromString(path.getFileName().toString().replace(EXTENSION, ""));
            Optional<T> objectNullable = findById(directory, id, clazz);
            objectNullable.ifPresent(objectList::add);
          });
    } catch (IOException e) {
      throw new RuntimeException("모든 파일 조회 중 오류 발생");
    }
    return objectList;
  }

  public static boolean existsById(Path directory, UUID id) {
    Path filePath = resolvePath(directory, id);
    return Files.exists(filePath);
  }

  public static void delete(Path directory, UUID id) {
    Path filePath = resolvePath(directory, id);
    try {
      Files.delete(filePath);
    } catch (IOException e) {
      throw new RuntimeException("파일 삭제 중 오류 발생");
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