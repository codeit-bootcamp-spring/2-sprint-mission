package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository {
    UUID createBinaryContent(BinaryContent binaryContent);
    BinaryContent findById(UUID id);
    List<BinaryContent> findAllByIdIn(List<UUID> idList);
    void deleteBinaryContent(UUID id);
    List<byte[]> findAll();
}
