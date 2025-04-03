package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.model.UserStatusType;
import com.sprint.mission.discodeit.repository.AbstractRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFUserStatusRepository  extends AbstractRepository<UserStatus> implements UserStatusRepository {
    private Map<UUID, UserStatus> userIdMap = new HashMap<>();

    public JCFUserStatusRepository() {
        super(UserStatus.class, new ConcurrentHashMap<>());
    }

    @Override
    public void add(UserStatus newUserStatus) {
        super.add(newUserStatus);
        userIdMap.put(newUserStatus.getUserId(), newUserStatus);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return userIdMap.containsKey(userId);
    }

    @Override
    public UserStatus findUserStatusByUserId(UUID userId) {
        if (!existsByUserId(userId)) {
            throw new NoSuchElementException("해당 userId를 가진 userStatus를 찾을 수 없습니다 : " + userId);
        }
        return userIdMap.get(userId);
    }

    @Override
    public void updateTimeById(UUID readStatusId, Instant updateTime) {
        super.storage.get(readStatusId).updateUpdatedAt(updateTime);
    }

    @Override
    public void updateTimeByUserId(UUID userId, Instant updateTime) {
        findUserStatusByUserId(userId).updateUpdatedAt(updateTime);
    }

    @Override
    public void updateUserStatusByUserId(UUID id, UserStatusType type) {
        findUserStatusByUserId(id).setUserStatusType(type);
    }

    @Override
    public void deleteById(UUID userStatusId) {
        super.deleteById(userStatusId);
        userIdMap.remove(userStatusId);
    }
}