package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.CreateReadStatusDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent create(CreateBinaryContentDto dto);
    BinaryContent find(UUID binaryContentKey);
    List<BinaryContent> findAllByKey(List<UUID> binaryContentKeys);
    void delete(UUID binaryContentKey);
}
