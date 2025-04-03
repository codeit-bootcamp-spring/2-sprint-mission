package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {

  void save();

  void addBinaryContent(BinaryContent content);

  Optional<BinaryContent> findBinaryContentById(UUID id);

  List<BinaryContent> findAllBinaryContents();

  void deleteBinaryContentById(UUID binaryContentId);

  boolean existsBinaryContent(UUID binaryContentId);
}
