package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> userStatusMap;

    public JCFUserStatusRepository() {
        this.userStatusMap = new HashMap<>();
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        this.userStatusMap.put(userStatus.getUserId(), userStatus);
        return userStatus;
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(userStatusMap.values());
    }

    @Override
    public Optional<UserStatus> findById(UUID userStatusId) {
        return Optional.ofNullable(userStatusMap.get(userStatusId));
    }

    @Override
    public boolean existsById(UUID userStatusId) {
        return userStatusMap.containsKey(userStatusId);
    }

    @Override
    public void deleteById(UUID userStatusId) {
        userStatusMap.remove(userStatusId);
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return Optional.ofNullable(userStatusMap.get(userId));
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return userStatusMap.containsKey(userId);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        userStatusMap.remove(userId);
    }
}
