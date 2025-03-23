package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    boolean register(BinaryContent binaryContent);
    Optional<BinaryContent> findById(UUID id);
    boolean update(BinaryContent binaryContent);
    boolean delete(BinaryContent binaryContent);
    List<UUID> findAll();
    List<BinaryContent> findAllByOwnerId(UUID ownerId);
    boolean delete(UUID userId);

}
