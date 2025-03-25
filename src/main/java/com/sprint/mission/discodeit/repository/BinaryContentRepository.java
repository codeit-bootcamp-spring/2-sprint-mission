package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository {

    BinaryContent save (BinaryContent binaryContent);
    BinaryContent findById(UUID id);
    List<BinaryContent> findAll(List<UUID> ids);
    void delete(UUID profileId);

    void deleteAllById(List<UUID> attachmentIds);
}
