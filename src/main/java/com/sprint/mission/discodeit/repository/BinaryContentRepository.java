package com.sprint.mission.discodeit.repository;


import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent save(BinaryContent binaryContent);
    Optional<BinaryContent> findById(UUID binaryContentUUID);
    List<BinaryContent> findAll();
    void delete(UUID profileId);
}
