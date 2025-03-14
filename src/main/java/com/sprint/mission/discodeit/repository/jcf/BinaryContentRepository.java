package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    Optional<BinaryContent> findById(UUID id);
    void save(BinaryContent binaryContent);
    void deleteById(UUID id);
}
