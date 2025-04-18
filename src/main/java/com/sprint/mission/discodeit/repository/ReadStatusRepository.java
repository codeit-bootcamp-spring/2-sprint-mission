package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadStatusRepository {

  ReadStatus save(ReadStatus readStatus);

  Optional<ReadStatus> findById(UUID id);

  List<ReadStatus> findAllByUser(User user);

  List<ReadStatus> findAllByChannel(Channel channel);

  boolean delete(ReadStatus readStatus);

  void deleteAllByChannel(Channel channel);

  void deleteAllByChannelId(UUID channelId);
}
