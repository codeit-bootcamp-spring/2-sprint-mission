package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity._Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {

  _Channel save(_Channel channel);

  Optional<_Channel> findById(UUID id);

  List<_Channel> findAll();

  boolean existsById(UUID id);

  void deleteById(UUID id);
}
