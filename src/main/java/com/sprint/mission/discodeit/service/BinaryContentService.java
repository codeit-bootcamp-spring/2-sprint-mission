package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.service.binaryContent.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.service.binaryContent.CreateBinaryContentParam;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContentDTO create(BinaryContent binaryContent);
    BinaryContentDTO find(UUID id);
    List<BinaryContentDTO> findAllByIdIn(List<UUID> attachmentsId);
    void delete(UUID id);
}
