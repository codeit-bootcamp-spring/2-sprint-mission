package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ReadStatusRepositoryImpl implements ReadStatusRepository {
    private final List<ReadStatus> readStatusList = new ArrayList<>();

    @Override
    public Optional<ReadStatus> findByUserAndChannel(UUID userId, UUID channelId) {
        return readStatusList.stream()
                .filter(rs -> rs.getUserId().equals(userId) && rs.getChannelId().equals(channelId))
                .findFirst();
    }

    @Override
    public List<ReadStatus> findAll() {
        return new ArrayList<>(readStatusList);
    }

    @Override
    public void save(ReadStatus readStatus) {
        //중복체크
        if (findByUserAndChannel(readStatus.getUserId(), readStatus.getChannelId()).isEmpty()) {
            readStatusList.add(readStatus);
        }
    }

    @Override
    public void updateLastReadAt(UUID userId, UUID channelId, Instant newLastReadAt) {
        findByUserAndChannel(userId, channelId).ifPresent(rs -> rs.updateLastReadAt(newLastReadAt));
    }

    @Override
    public void delete(UUID userId, UUID channelId) {
        readStatusList.removeIf(rs -> rs.getUserId().equals(userId) && rs.getChannelId().equals(channelId));
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        readStatusList.removeIf(r -> r.getChannelId().equals(channelId));
    }

    @Override
    public List<UUID> findUserIdsByChannelId(UUID channelId) {
        return readStatusList.stream()
                .filter(rs -> rs.getChannelId().equals(channelId))
                .map( rs -> rs.getUserId())
                .toList();
    }
}
