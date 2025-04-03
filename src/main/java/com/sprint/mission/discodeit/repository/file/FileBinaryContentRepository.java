package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileBinaryContentRepository implements BinaryContentRepository {

  private final String filePath;
  private final Map<UUID, BinaryContent> data;

  public FileBinaryContentRepository(
      @Value("${discodeit.repository.file-directory}") String baseDir) {
    this.filePath = baseDir + "/binaryContent.ser";
    this.data = loadData();
  }

  private void saveData() throws IOException {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
      oos.writeObject(data);
    }
  }

  private Map<UUID, BinaryContent> loadData() {
    File file = new File(filePath);
    if (!file.exists()) {
      return new HashMap<>();
    }
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
      return (Map<UUID, BinaryContent>) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return new HashMap<>();
    }
  }

  @Override
  public BinaryContent save(BinaryContent binaryContent) {
    data.put(binaryContent.getId(), binaryContent);
    try {
      saveData();
    } catch (IOException e) {
      throw new RuntimeException("BinaryContent 저장 중 오류 발생", e);
    }
    return binaryContent;
  }

  @Override
  public Optional<BinaryContent> getById(UUID id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public List<BinaryContent> getAll() {
    return new ArrayList<>(data.values());
  }

  @Override
  public void deleteById(UUID id) {
    data.remove(id);
    try {
      saveData();
    } catch (IOException e) {
      throw new RuntimeException("BinaryContent 삭제 중 오류 발생", e);
    }
  }
}
