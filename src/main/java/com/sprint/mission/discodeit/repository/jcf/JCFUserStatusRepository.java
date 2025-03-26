package com.sprint.mission.discodeit.repository.jcf;


import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> userStatusData;

    public JCFUserStatusRepository() {
        this.userStatusData = new HashMap<>();
    }

    @Override
    public UserStatus saveUserStatus(UserStatus userStatus) {
        this.userStatusData.put(userStatus.getUserStatusId(), userStatus);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID userStatusId) {
        return Optional.ofNullable(this.userStatusData.get(userStatusId));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return this.findAll().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return this.userStatusData.values().stream().toList();
    }

    @Override
    public boolean existsById(UUID userStatusId) {
        return this.userStatusData.containsKey(userStatusId);
    }

    @Override
    public void deleteById(UUID userStatusId) {
        this.userStatusData.remove(userStatusId);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        this.findByUserId(userId)
                .ifPresent(userStatus -> this.deleteById(userStatus.getUserStatusId()));
    }
}
