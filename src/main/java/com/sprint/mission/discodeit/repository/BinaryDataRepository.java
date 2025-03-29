package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryData;

import java.util.Optional;
import java.util.UUID;

public interface BinaryDataRepository {
    BinaryData save(BinaryData binaryData);
    Optional<BinaryData> findById(UUID binaryContentUUID);
}
