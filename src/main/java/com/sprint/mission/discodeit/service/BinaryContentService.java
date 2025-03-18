package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentFindResponse;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    UUID create(BinaryContentCreateRequest binaryContentCreateRequest);
    boolean existsById(UUID id);
    BinaryContentFindResponse findById(UUID id);
    List<BinaryContentFindResponse> findAllByIdIn(List<UUID> ids);
    void deleteByID(UUID id);
}
