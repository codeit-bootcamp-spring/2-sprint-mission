package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity._BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {

  private final Map<UUID, _BinaryContent> data;

  public JCFBinaryContentRepository() {
    this.data = new HashMap<>();
  }

  @Override
  public _BinaryContent save(_BinaryContent binaryContent) {
    this.data.put(binaryContent.getId(), binaryContent);
    return binaryContent;
  }

  @Override
  public Optional<_BinaryContent> findById(UUID id) {
    return Optional.ofNullable(this.data.get(id));
  }

  @Override
  public List<_BinaryContent> findAllByIdIn(List<UUID> ids) {
    return this.data.values().stream()
        .filter(content -> ids.contains(content.getId()))
        .toList();
  }

  @Override
  public boolean existsById(UUID id) {
    return this.data.containsKey(id);
  }

  @Override
  public void deleteById(UUID id) {
    this.data.remove(id);
  }
}
