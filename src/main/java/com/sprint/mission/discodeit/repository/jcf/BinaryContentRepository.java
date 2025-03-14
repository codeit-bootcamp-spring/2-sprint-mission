package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent findById(UUID id);
    void save(BinaryContent binaryContent);
}
