package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.BinaryDataResponseDto;
import com.sprint.mission.discodeit.entity.BinaryData;

public interface BinaryDataRepository {
    BinaryDataResponseDto save(BinaryData binaryData);
}
