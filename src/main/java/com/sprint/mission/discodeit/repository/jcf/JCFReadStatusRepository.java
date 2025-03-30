package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFReadStatusRepository implements ReadStatusRepository {

    private final Map<UUID, ReadStatus> readStatusData = new HashMap<>();


    @Override
    public ReadStatus save(ReadStatus readStatus) {
        readStatusData.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public ReadStatus find(UUID id) {
        return readStatusData.get(id);
    }

    @Override
    public List<ReadStatus> findAll() {
        return readStatusData.values().stream().toList();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatusData.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return readStatusData.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public ReadStatus update(UUID id) {
        ReadStatus readStatusNullable = readStatusData.get(id);
        ReadStatus readStatus = Optional.ofNullable(readStatusNullable).orElseThrow(() -> new NoSuchElementException(("상태를 찾을 수 없습니다.")));
        readStatus.updateLastCheckedAt();
        return readStatus;
    }

    @Override
    public void delete(UUID id) {
        readStatusData.remove(id);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        readStatusData.entrySet().removeIf(entry -> entry.getValue().getChannelId().equals(channelId));
    }
}
