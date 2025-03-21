package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.service.binaryContent.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.service.binaryContent.CreateBinaryContentParam;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContentDTO create(CreateBinaryContentParam createBinaryContentParam);
    BinaryContentDTO find(UUID id);
    List<BinaryContentDTO> findAllByIdIn(List<UUID> attachmentsId);
    void delete(UUID id);
}
