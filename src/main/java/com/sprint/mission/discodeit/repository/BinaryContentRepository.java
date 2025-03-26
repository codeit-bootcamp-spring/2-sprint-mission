package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent save(BinaryContent binaryContent);
    Optional<BinaryContent> findById(UUID id);
    List<BinaryContent> findByFileName(String fileName);
    List<BinaryContent> findByContentType(String contentType);
    List<BinaryContent> findAll();
    void deleteById(UUID id);
    List<BinaryContent> findAllByIds(List<UUID> ids);
}
