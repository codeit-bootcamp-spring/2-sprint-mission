package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.SaveReadStatusParamDto;
import com.sprint.mission.discodeit.dto.UpdateReadStatusParamDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    void save(SaveReadStatusParamDto saveReadStatusParamDto);
    ReadStatus findById(UUID readStatusUUID);
    List<ReadStatus> findAllByUserId(UUID userUUID);
    void update(UpdateReadStatusParamDto updateReadStatusParamDto);
    void delete(UUID readStatusUUID);
}
