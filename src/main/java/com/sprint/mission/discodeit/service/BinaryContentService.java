package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent create(String filePath);
    BinaryContent findById(UUID binaryContentId);
    List<BinaryContent> findAll();
    void delete(UUID binaryContentId);
}
