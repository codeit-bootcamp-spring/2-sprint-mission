package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent save(BinaryContent content);

    Optional<BinaryContent> findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);
}
