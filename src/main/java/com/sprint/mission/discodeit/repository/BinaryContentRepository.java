package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent save(BinaryContent binaryContent);
    Optional<BinaryContent> findById(UUID id);
    List<BinaryContent> findAll();
    void delete(UUID id);
}
