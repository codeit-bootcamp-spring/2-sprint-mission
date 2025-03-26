package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserStatusRepositoryImpl implements UserStatusRepository {

    private final Map<UUID, UserStatus> userStatusStore = new ConcurrentHashMap<>();

    @Override
    public UserStatus save(UserStatus userStatus) {
        userStatusStore.put(userStatus.getId(), userStatus);
        return userStatus;
    }

    @Override
    public void deleteByUserId(UUID userId) {

    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(userStatusStore.get(id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return userStatusStore.values()
                .stream()
                .filter(us -> us.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(userStatusStore.values());
    }

    @Override
    public boolean existsById(UUID id) {
        return userStatusStore.containsKey(id);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return userStatusStore.values().stream().anyMatch(us -> us.getUserId().equals(userId));
    }

    @Override
    public void deleteById(UUID id) {
        userStatusStore.remove(id);
    }
}