package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BinaryContentRepository {
    BinaryContent findById(UUID id);
    void save(BinaryContent binaryContent);
    void saveAll(List<BinaryContent> binaryContents);
    void deleteByUserId(UUID userId);
    void deleteByMessageId(UUID messageId);
}
