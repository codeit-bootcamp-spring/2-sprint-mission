package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.AbstractRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFReadStatusRepository extends AbstractRepository<ReadStatus> implements ReadStatusRepository {
    private final Map<UUID, List<ReadStatus>> userIdMap;
    private final Map<UUID, List<ReadStatus>> channelIdMap;

    public JCFReadStatusRepository() {
        super(ReadStatus.class, new ConcurrentHashMap<>());      // 현재 프로그램이 실행되고 있는 디렉토리로 설정);
        this.userIdMap = new HashMap<>();
        this.channelIdMap = new HashMap<>();
    }

    @Override
    public void addUserIdMap(UUID userId) {
        this.userIdMap.put(userId, new ArrayList<>());
    }

    @Override
    public void addChannelIdMap(UUID channelId) {
        this.channelIdMap.put(channelId, new ArrayList<>());
    }

    @Override
    public void add (ReadStatus newReadStatus) {
        super.add(newReadStatus);
        this.userIdMap.get(newReadStatus.getUserId()).add(newReadStatus);
        this.channelIdMap.get(newReadStatus.getChannelId()).add(newReadStatus);
    }

    @Override
    public List<ReadStatus> findByUserId(UUID userId) {
        if (!userIdMap.containsKey(userId)) {
            throw new NoSuchElementException("해당 userId를 가진 readStatus를 찾을 수 없습니다 : " + userId);
        }
        return this.userIdMap.get(userId);
    }

    @Override
    public List<ReadStatus> findByChannelId(UUID channelId) {
        if (!channelIdMap.containsKey(channelId)) {
            throw new NoSuchElementException("해당 channelId를 가진 readStatus를 찾을 수 없습니다 : " + channelId);
        }
        return this.channelIdMap.get(channelId);
    }

    @Override
    public void updateReadTime(UUID readStatusId, Instant readTime) {
        super.findById(readStatusId).updateReadTime(readTime);
    }

    @Override
    public void deleteById(UUID readStatusId) {
        ReadStatus target = super.findById(readStatusId);
        this.userIdMap.get(super.findById(readStatusId).getUserId()).remove(target);
        this.channelIdMap.get(super.findById(readStatusId).getChannelId()).remove(target);
        super.deleteById(readStatusId);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        this.userIdMap.remove(userId);
        List<ReadStatus> readStatusList = this.findByUserId(userId);
        readStatusList.forEach(readStatus -> channelIdMap.get(readStatus.getChannelId()).remove(readStatus));
        readStatusList.forEach(readStatus -> this.deleteById(readStatus.getId()));
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        List<ReadStatus> readStatusList = this.findByChannelId(channelId);
        this.channelIdMap.remove(channelId);
        readStatusList.forEach(readStatus -> userIdMap.get(readStatus.getUserId()).remove(readStatus));
        readStatusList.forEach(readStatus -> this.deleteById(readStatus.getId()));
    }
}
