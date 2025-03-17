package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BinaryContentRepository {
    BinaryContent findById(UUID id);
    void save(BinaryContent binaryContent);
    void deleteByUserId(UUID userId);
}
