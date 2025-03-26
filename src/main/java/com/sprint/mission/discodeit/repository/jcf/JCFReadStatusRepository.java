package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.Empty.EmptyReadStatusListException;
import com.sprint.mission.discodeit.exception.NotFound.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.util.CommonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> readStatusList = new ConcurrentHashMap<>();

//    private final List<ReadStatus> readStatusList = new ArrayList<>();

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        readStatusList.put(readStatus.getReadStatusId(), readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> find(UUID readStatusId) {
        return Optional.ofNullable(this.readStatusList.get(readStatusId));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userID) {
        if (readStatusList.isEmpty()) {
            throw new EmptyReadStatusListException("Repository 에 저장된 읽기 상태 리스트가 없습니다.");
        }

        List<ReadStatus> list = this.readStatusList.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userID))
                .toList();

        if (list.isEmpty()) {
            throw new EmptyReadStatusListException("해당 서버에 저장된 읽기 상태 리스트가 없습니다.");
        }
        return list;
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        if (readStatusList.isEmpty()) {
            throw new EmptyReadStatusListException("Repository 에 저장된 읽기 상태 리스트가 없습니다.");
        }

        List<ReadStatus> list = this.readStatusList.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .toList();

        if (list.isEmpty()) {
            throw new EmptyReadStatusListException("해당 서버에 저장된 읽기 상태 리스트가 없습니다.");
        }
        return list;
    }

    @Override
    public void delete(UUID readStatusId) {
        readStatusList.remove(readStatusId);
    }
}
