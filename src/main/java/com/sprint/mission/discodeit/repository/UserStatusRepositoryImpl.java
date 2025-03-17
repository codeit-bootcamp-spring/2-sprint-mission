package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserStatusRepositoryImpl implements UserStatusRepository {
    private final Map<UUID, UserStatus> userStatusStore = new HashMap<>(); // ✅ 메모리 저장소 사용

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return Optional.ofNullable(userStatusStore.get(userId));
    }

    @Override
    public void save(UserStatus userStatus) {
        userStatusStore.put(userStatus.getUserId(), userStatus);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        userStatusStore.remove(userId);
    }
}