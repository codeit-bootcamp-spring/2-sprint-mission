package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {
    // 인메모리 저장소: UUID를 키로 사용
    private final Map<UUID, ReadStatus> store = new ConcurrentHashMap<>();

    /**
     * 사용자와 채널 식별자로 읽음 상태를 조회
     */
    @Override
    public ReadStatus findByUserAndChannel(UUID userId, UUID channelId) {
        return store.values().stream()
                .filter(rs -> rs.getUserId().equals(userId) && rs.getChannelId().equals(channelId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public ReadStatus findById(UUID id) {
        return store.get(id);
    }

    @Override
    public List<ReadStatus> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void save(ReadStatus readStatus) {
        if (readStatus.getId() == null) {
            readStatus.setId(UUID.randomUUID());
        }
        store.put(readStatus.getId(), readStatus);
    }

    @Override
    public void delete(ReadStatus readStatus) {
        store.remove(readStatus.getId());
    }
}
