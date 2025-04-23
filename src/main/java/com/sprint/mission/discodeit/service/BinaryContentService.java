package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentDto;
import java.util.List;
import java.util.UUID;

public interface BinaryContentService {

    BinaryContentDto create(BinaryContentDto binaryContentDto);

    BinaryContentDto findById(UUID id);

    List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds);

    void deleteById(UUID contentId);
}
