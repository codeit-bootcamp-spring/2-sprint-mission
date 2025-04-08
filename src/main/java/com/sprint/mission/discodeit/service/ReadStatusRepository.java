package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {

  void saveData();

  void register(ReadStatus readStatus);

  Optional<ReadStatus> findById(UUID id);

  List<ReadStatus> findAll();

  List<ReadStatus> findAllByUserId(UUID userId);

  Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);

  boolean updateReadStatus(ReadStatus readStatus);

  boolean deleteReadStatus(UUID id);

  boolean deleteAllByChannelId(UUID channelId);

  boolean deleteAllByUserId(UUID userId);
} 