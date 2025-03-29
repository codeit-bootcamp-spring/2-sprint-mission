package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.FindBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.SaveBinaryContentParamDto;
import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentService {
    BinaryContent save(SaveBinaryContentParamDto saveBinaryContentParamDto);
    FindBinaryContentRequestDto findById(UUID binaryContentUUID);
    List<FindBinaryContentRequestDto> findByIdIn(List<UUID> binaryContentUUIDList);
    void delete(UUID userStatusUUID);
}
