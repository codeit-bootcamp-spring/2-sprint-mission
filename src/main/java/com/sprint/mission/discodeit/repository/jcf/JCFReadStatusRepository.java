package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFReadStatusRepository implements ReadStatusRepository {

    private final Map<UUID, ReadStatus> data = new HashMap<>();

    @Override
    public UUID createReadStatus(ReadStatus readStatus) {
        validateReadStatusDoesNotExist(readStatus.getUserId(), readStatus.getChannelId());

        data.put(readStatus.getId(), readStatus);
        return findById(readStatus.getId()).getId();
    }

    @Override
    public ReadStatus findById(UUID id) {
        ReadStatus readStatus = data.get(id);
        if (readStatus == null) {
            throw new NoSuchElementException("해당 ID의 ReadStatus를 찾을 수 없습니다: " + id);
        }
        return readStatus;
    }

    @Override
    public ReadStatus findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return data.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId) && readStatus.getChannelId().equals(channelId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 사용자 및 채널의 ReadStatus를 찾을 수 없습니다: " + userId + "/" + channelId));
    }

    public ReadStatus findByUserIdAndChannelIdOrNull(UUID userId, UUID channelId) {
        return data.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId) && readStatus.getChannelId().equals(channelId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return data.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
    }

    @Override
    public void updateReadStatus(UUID id, Instant lastReadAt) {
        checkReadStatusExists(id);
        ReadStatus readStatus = data.get(id);

        readStatus.update(lastReadAt);
    }

    @Override
    public void deleteReadStatus(UUID id) {
        checkReadStatusExists(id);

        data.remove(id);
    }

    @Override
    public void deleteReadStatusByChannelId(UUID channelId) {
        ReadStatus readStatus = data.values().stream()
                .filter(result -> result.getChannelId().equals(channelId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 채널의 ReadStatus를 찾을 수 없습니다: " + channelId));

        data.remove(readStatus.getId());
    }

    /*******************************
     * Validation check
     *******************************/
    private void checkReadStatusExists(UUID id) {
        if(findById(id) == null){
            throw new NoSuchElementException("해당 ID의 ReadStatus를 찾을 수 없습니다: " + id);
        }
    }

    private void validateReadStatusDoesNotExist(UUID userId, UUID channelId) {
        if (findByUserIdAndChannelIdOrNull(userId, channelId) != null) {
            throw new IllegalArgumentException("이미 존재하는 객체입니다.");
        }
    }

}
