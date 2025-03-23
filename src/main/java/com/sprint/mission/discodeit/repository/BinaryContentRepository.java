package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BinaryContentRepository {
    Optional<BinaryContent> findByUserId(UUID id);
    List<BinaryContent> findAll();
    List<BinaryContent> findAllById(List<UUID> ids);
    void save(BinaryContent binaryContent);
    void delete(BinaryContent binaryContent);
}
