package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return Optional.ofNullable(storage.get(userId));
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        if (userStatus.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        storage.put(userStatus.getUserId(), userStatus);
        return userStatus;
    }

    @Override
    public void deleteByUserId(UUID userId) {
        storage.remove(userId);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return storage.containsKey(userId);
    }

    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(storage.values());
    }
}