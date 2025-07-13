package com.sprint.mission.discodeit.core.read.service;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.read.dto.ReadStatusDto;
import com.sprint.mission.discodeit.core.read.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.core.read.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.core.user.entity.User;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public interface ReadStatusService {

  ReadStatusDto create(ReadStatusCreateRequest request);

  ReadStatusDto create(User user, Channel channel);

  ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request);

  void delete(UUID readStatusId);

}
