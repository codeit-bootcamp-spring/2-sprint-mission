package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;


public interface ReadStatusService {

    ReadStatusDto.ResponseReadStatus create(ReadStatusDto.Create readStatusDto);
    ReadStatusDto.ResponseReadStatus find(UUID id);
    List<ReadStatusDto.ResponseReadStatus> findAllByUserId(UUID userId);
    ReadStatusDto.ResponseReadStatus findByUserIdAndChannelId(UUID userId, UUID channelId);
    ReadStatusDto.ResponseReadStatus update(ReadStatusDto.Update updateDto);
    boolean delete(UUID id);
    boolean deleteAllByUserId(UUID userId);
    boolean deleteAllByChannelId(UUID channelId);
    Integer getUnreadMessageCount(UUID userId, UUID channelId);
}
