package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BinaryContentRepository {
    BinaryContent save(BinaryContent binaryContent);
    Optional<BinaryContent> findById(UUID id);
    boolean existsById(UUID id);
    void deleteById(UUID id);
    List<BinaryContent> findAllById(List<UUID> ids);

    void saveAll(List<BinaryContent> binaryContents);

    void deleteByUserId(UUID userId);

    void deleteByMessageId(UUID messageId);
}
