package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent create(String filePath);
    BinaryContent findById(UUID binaryContentId);
    void delete(UUID binaryContentId);
}
