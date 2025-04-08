package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent save(BinaryContent binaryContent);
    BinaryContent findByKey(UUID key);
    List<BinaryContent> findAllByKeys(List<UUID> binaryKeys);
    boolean existsByKey(UUID binaryKey);
    void delete(UUID key);
}
