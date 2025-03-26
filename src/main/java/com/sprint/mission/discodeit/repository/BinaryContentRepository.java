package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository {
    void save();

    void addBinaryContent(BinaryContent content);

    BinaryContent findBinaryContentById(UUID id);

    List<BinaryContent> findAllBinaryContents();

    void deleteBinaryContentById(UUID binaryContentId);

    boolean existsBinaryContent(UUID binaryContentId);
}
