package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    void save(BinaryContent binaryContent);
    Optional<BinaryContent> getById(UUID id);
    List<BinaryContent> getAll();
    void deleteById(UUID id);
    List<BinaryContent> getByUserId(UUID userId);
    List<BinaryContent> getByMessageId(UUID messageId);
}
