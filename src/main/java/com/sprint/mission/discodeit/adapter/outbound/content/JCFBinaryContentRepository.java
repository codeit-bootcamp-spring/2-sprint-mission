package com.sprint.mission.discodeit.adapter.outbound.content;

import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import com.sprint.mission.discodeit.core.content.port.BinaryContentRepositoryPort;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFBinaryContentRepository implements BinaryContentRepositoryPort {

  private final Map<UUID, BinaryContent> binaryContentList = new ConcurrentHashMap<>();

  @Override
  public BinaryContent save(BinaryContent binaryContent) {
    binaryContentList.put(binaryContent.getId(), binaryContent);
    return binaryContent;
  }

  @Override
  public BinaryContent findById(UUID binaryId) {
    return binaryContentList.get(binaryId);
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
    return binaryContentList.values().stream().filter(content -> ids.contains(content.getId()))
        .toList();
  }

  @Override
  public void delete(UUID binaryId) {
    binaryContentList.remove(binaryId);
  }
}
