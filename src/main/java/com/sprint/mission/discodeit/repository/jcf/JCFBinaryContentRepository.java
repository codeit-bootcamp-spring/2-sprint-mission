package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFBinaryContentRepository implements BinaryContentRepository {

  private final Map<UUID, BinaryContent> data = new HashMap<>();

  public JCFBinaryContentRepository() {
  }

  @Override
  public BinaryContent save(BinaryContent binaryContent) {
    data.put(binaryContent.getId(), binaryContent);
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
  }
}
