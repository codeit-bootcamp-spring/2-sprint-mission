package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> userStatusRepository = new HashMap<>();

    @Override
    public UserStatus save(UserStatus userStatus) {
        return userStatusRepository.put(userStatus.getId(), userStatus);
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(userStatusRepository.get(id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return userStatusRepository.values()
                .stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public void delete(UUID id) {
        userStatusRepository.remove(id);
    }
}
