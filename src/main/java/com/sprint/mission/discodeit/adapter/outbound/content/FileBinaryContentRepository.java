package com.sprint.mission.discodeit.adapter.outbound.content;

import com.sprint.mission.discodeit.adapter.outbound.FileRepositoryImpl;
import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.port.BinaryContentRepositoryPort;
import com.sprint.mission.discodeit.exception.SaveFileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileBinaryContentRepository implements BinaryContentRepositoryPort {

  private final FileRepositoryImpl<Map<UUID, BinaryContent>> fileRepository;
  private final Path path = Paths.get(System.getProperty("user.dir"), "data",
      "BinaryContentList.ser");
  private Map<UUID, BinaryContent> binaryContentList = new ConcurrentHashMap<>();

  public FileBinaryContentRepository() {
    this.fileRepository = new FileRepositoryImpl<>(path);
    try {
      this.binaryContentList = fileRepository.load();
    } catch (SaveFileNotFoundException e) {
      System.out.println("FileBinaryContentRepository init");
    }
  }

  @Override
  public BinaryContent save(BinaryContent binaryContent) {
    binaryContentList.put(binaryContent.getId(), binaryContent);
    fileRepository.save(binaryContentList);
    return binaryContent;
  }

  @Override
  public BinaryContent findById(UUID binaryId) {
    return binaryContentList.get(binaryId);
  }

  @Override
  public List<BinaryContent> findAllByIdIn() {
    return binaryContentList.values().stream().toList();
  }

  @Override
  public void delete(UUID binaryId) {
    binaryContentList.remove(binaryId);
    fileRepository.save(binaryContentList);
  }
}
