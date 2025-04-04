package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.*;


import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    BinaryContent create(BinaryContentCreateDto binaryContentCreateDto);
    BinaryContent find(UUID binaryContentId);
    List<BinaryContent> findAll(List<UUID> binaryContentIds);
    BinaryContent updateByUserId(BinaryContentUpdateDto binaryContentUpdateDto);
    void delete(UUID binaryContentId);

}
