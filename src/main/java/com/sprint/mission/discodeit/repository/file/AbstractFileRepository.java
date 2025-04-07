package com.sprint.mission.discodeit.repository.file;

import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractFileRepository<T> {

  protected final Path path;

  protected AbstractFileRepository(String directory, String fileName) {
    this.path = Paths.get(directory, fileName);
    init(path.getParent());
  }

  @SuppressWarnings("unchecked")
  protected T loadData() {
    if (Files.exists(path)) {
      try (InputStream is = Files.newInputStream(path);
          ObjectInputStream ois = new ObjectInputStream(is)) {
        return (T) ois.readObject();
      } catch (IOException | ClassNotFoundException e) {
        throw new RuntimeException("데이터 로드 실패");
      }
    }

    return getEmptyData();
  }

  protected void saveData(T data) {
    try (OutputStream os = Files.newOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(os)) {
      oos.writeObject(data);
    } catch (IOException e) {
      throw new RuntimeException("메세지 데이터 저장 실패");
    }
  }

  protected abstract T getEmptyData();

  private void init(Path directory) {
    if (!Files.exists(directory)) {
      try {
        Files.createDirectories(directory);
      } catch (IOException e) {
        throw new RuntimeException("디렉토리 생성 안 됨");
      }
    }
  }
}
