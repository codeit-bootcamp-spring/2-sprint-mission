package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.dto.binarycontentdto.*;


import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    BinaryContentResponseDto create(BinaryContentCreateDto binaryContentCreateDto);
    BinaryContentResponseDto find(UUID binaryContentId);
    List<BinaryContentResponseDto> findAll(List<UUID> binaryContentIds);
    BinaryContentResponseDto updateByUserId(BinaryContentUpdateDto binaryContentUpdateDto);
    void delete(UUID binaryContentId);

}
