package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(
        name = "discordit.repository.type",
        havingValue = "jcf")
public class JCFReadStatusRepository implements ReadStatusRepository {
    private static final Map<UUID, ReadStatus> readStatusMap = new HashMap<>();

    @Override
    public void save() {
    }

    @Override
    public void addReadStatus(ReadStatus readStatus) {
        readStatusMap.put(readStatus.getId(), readStatus);
    }

    @Override
    public ReadStatus findReadStatusById(UUID userId, UUID channelId) {
        return readStatusMap.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<ReadStatus> findAllReadStatus() {
        return new ArrayList<>(readStatusMap.values());
    }


    @Override
    public void deleteReadStatusById(UUID id) {
        readStatusMap.remove(id);
    }

    @Override
    public boolean existReadStatusById(UUID id) {
        return readStatusMap.containsKey(id);
    }
}
