package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository {

    UUID save(BinaryContent binaryContent);

    BinaryContent find(UUID id);

    List<BinaryContent> findAll();

    void delete(UUID profileImageId);
}
