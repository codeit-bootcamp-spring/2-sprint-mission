package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = true)
public class FileBinaryContentRepository implements BinaryContentRepository {

  private final Path DIRECTORY;
  private final String EXTENSION = ".ser";

  public FileBinaryContentRepository(RepositoryProperties properties) {
    this.DIRECTORY = Paths.get(properties.getBinaryContent());
    FileUtil.init(DIRECTORY);
  }

  @Override
  public BinaryContent save(BinaryContent binaryContent) {
    return FileUtil.saveToFile(DIRECTORY, binaryContent, binaryContent.getId());
  }

  @Override
  public Optional<BinaryContent> findById(UUID id) {
    return FileUtil.loadFromFile(DIRECTORY, id);
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> idList) {
    return FileUtil.loadAllFiles(DIRECTORY, EXTENSION).stream()
        .filter(BinaryContent.class::isInstance)
        .map(BinaryContent.class::cast)
        .filter(object -> idList.contains(object.getId()))
        //.filter(binaryContent -> idSet.contains(binaryContent.getId()))
        .collect(Collectors.toList());
  }

  @Override
  public void deleteById(UUID id) {
    FileUtil.deleteFile(DIRECTORY, id);
  }


}
