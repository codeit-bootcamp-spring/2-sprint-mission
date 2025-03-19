package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.SaveBinaryContentParamDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    void save(SaveBinaryContentParamDto saveBinaryContentParamDto);
    BinaryContent findById(UUID binaryContentUUID);
    List<BinaryContent> findByIdIn(List<UUID> binaryContentUUIDList);
    void delete(UUID userStatusUUID);
}
