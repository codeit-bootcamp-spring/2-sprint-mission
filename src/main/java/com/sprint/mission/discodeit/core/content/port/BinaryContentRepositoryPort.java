package com.sprint.mission.discodeit.core.content.port;

import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepositoryPort {

  BinaryContent save(BinaryContent binaryContent);

  Optional<BinaryContent> findById(UUID binaryId);

  List<BinaryContent> findAllByIdIn(List<UUID> ids);

  boolean existsId(UUID binaryId);

  void delete(UUID binaryId);
}
