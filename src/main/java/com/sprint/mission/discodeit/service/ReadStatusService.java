package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.service.readStatus.CreateReadStatusCommand;
import com.sprint.mission.discodeit.dto.service.readStatus.CreateReadStatusResult;
import com.sprint.mission.discodeit.dto.service.readStatus.FindReadStatusResult;
import com.sprint.mission.discodeit.dto.service.readStatus.UpdateReadStatusCommand;
import com.sprint.mission.discodeit.dto.service.readStatus.UpdateReadStatusResult;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  CreateReadStatusResult create(CreateReadStatusCommand createReadStatusCommand);

  FindReadStatusResult find(UUID id);

  List<FindReadStatusResult> findAllByUserId(UUID userId);

  List<FindReadStatusResult> findAllByChannelId(UUID channelId);

  UpdateReadStatusResult update(UUID id, UpdateReadStatusCommand updateReadStatusCommand);

  void delete(UUID id);

  void deleteByChannelId(UUID channelId);
}
