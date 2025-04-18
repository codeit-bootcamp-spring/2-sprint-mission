package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository {

  ReadStatus save(ReadStatusCreateRequest request);

  ReadStatus findById(UUID id);

  List<ReadStatus> findAll();

  ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest request);

  void delete(UUID readStatusID);

  List<ReadStatus> findAllByChannelId(UUID channelId);
}
