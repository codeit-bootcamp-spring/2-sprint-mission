package com.sprint.mission.discodeit.core.content.port;

import com.sprint.mission.discodeit.core.content.entity.BinaryContent;
import java.util.Optional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BinaryContentRepositoryPort {

  BinaryContent save(BinaryContent binaryContent);

  Optional<BinaryContent> findById(UUID binaryId);

  List<BinaryContent> findAllByIdIn(List<UUID> ids);

  boolean existsId(UUID binaryId);

  void delete(UUID binaryId);
}
