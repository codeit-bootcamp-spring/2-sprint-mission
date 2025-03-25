package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);

    Optional<ReadStatus> findById(UUID readStatusId);

    List<ReadStatus> findAllByUserId(UUID userId);

    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);

    void deleteById(UUID readStatusId);

    boolean existsById(UUID readStatusId);

}
/*@Override
public List<ReadStatus> findByUserId(UUID userId) {
    return readStatusList.stream()
            .filter(readStatus -> readStatus.getUserId().equals(userId))
            .toList();
}*/