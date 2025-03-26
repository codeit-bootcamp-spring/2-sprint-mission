package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusReqDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResDto;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusReqDto;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusResDto create(CreateReadStatusReqDto createReadStatusReqDto);
    ReadStatusResDto find(UUID readStatusId);
    List<ReadStatusResDto> findAllByUserId(UUID userId);
    ReadStatusResDto update(UUID readStatusId, UpdateReadStatusReqDto updateReadStatusReqDto);
    void delete(UUID readStatusId);
}
