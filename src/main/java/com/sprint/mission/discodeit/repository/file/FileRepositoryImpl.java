package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Identifiable;
import com.sprint.mission.discodeit.repository.FileRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Repository
public class FileRepositoryImpl<T extends Identifiable> implements FileRepository<T> {

  @Override
  public void saveToFile(T t, Path directory) {
    Path filePath = directory.resolve(t.getId() + ".ser");
    SerializationUtil.init(directory);
    SerializationUtil.serialization(filePath, t);
  }

  @Override
  public List<T> loadAllFromFile(Path directory) {
    return SerializationUtil.reverseSerialization(directory);
  }

  @Override
  public void deleteFileById(UUID id, Path directory) {
    Path filePath = directory.resolve(id + ".ser");
    try {
      Files.deleteIfExists(filePath);
    } catch (IOException e) {
      System.out.println(id + "파일 삭제 예외 발생 : " + e.getMessage());
    }
  }
}
