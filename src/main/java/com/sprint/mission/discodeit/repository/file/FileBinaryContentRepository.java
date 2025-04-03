package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.common.BinaryContent;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(value = "discodeit.repository.type", havingValue = "file", matchIfMissing = false)
public class FileBinaryContentRepository extends
    AbstractFileRepository<Map<UUID, BinaryContent>> implements BinaryContentRepository {

  private Map<UUID, BinaryContent> data;

  public FileBinaryContentRepository(
      @Value("${discodeit.repository.file-directory}") String directory) {
    super(directory, BinaryContent.class.getSimpleName() + ".ser");
    this.data = loadData();
  }

  @Override
  protected Map<UUID, BinaryContent> getEmptyData() {
    return new HashMap<>();
  }

  @Override
  public BinaryContent save(BinaryContent binaryContent) {
    data.put(binaryContent.getId(), binaryContent);
    saveData(data);
    return binaryContent;
  }

  @Override
  public Optional<BinaryContent> findById(UUID id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
    return ids.stream()
        .map(data::get)
        .toList();
  }

  @Override
  public boolean existsById(UUID id) {
    return data.containsKey(id);
  }

  @Override
  public void deleteById(UUID id) {
    data.remove(id);
    saveData(data);
  }
}
