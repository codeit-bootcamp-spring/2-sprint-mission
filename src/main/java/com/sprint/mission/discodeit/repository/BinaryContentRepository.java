package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity._BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {

  _BinaryContent save(_BinaryContent binaryContent);

  Optional<_BinaryContent> findById(UUID id);

  List<_BinaryContent> findAllByIdIn(List<UUID> ids);

  boolean existsById(UUID id);

  void deleteById(UUID id);
}
