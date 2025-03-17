package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository {
    void save();

    void addBinaryContent(BinaryContent content);

    BinaryContent findBinaryContent(UUID binaryContentId);

    List<BinaryContent> findAllBinaryContents();

    void deleteBinaryContent(UUID binaryContentId);

    boolean existsBinaryContent(UUID binaryContentId);
}
