package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(ReadStatusCreateRequest request);
    ReadStatus find(UUID readStatusId);
    List<ReadStatus> findAllByUserId(UUID userId);
    ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest request);
    void delete(UUID readStatusId);
    List<ReadStatus> findAllByUser(User user);
    List<ReadStatus> findAllByChannel(Channel channel);
    void deleteAllByChannel(Channel channel);

}
