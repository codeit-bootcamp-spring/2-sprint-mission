package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.UpdateReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(CreateReadStatusDto dto);
    ReadStatus find(UUID readStatusKey);
    UpdateReadStatusDto update(UpdateReadStatusDto dto);
    List<ReadStatus> findAllByUserKey(UUID userKey);
    void delete(UUID readStatusKey);
}
