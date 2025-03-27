package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> userStatusList = new HashMap<>();

    @Override
    public void save(UserStatus userStatus) {
        userStatusList.put(userStatus.getUserUUID(), userStatus);
    }

    @Override
    public Optional<UserStatus> findById(UUID userStatusUUID) {
        return Optional.of(userStatusList.get(userStatusUUID));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userUUID) {
        return userStatusList.values().stream()
                .filter(userStatus -> userStatus.getUserUUID().equals(userUUID))
                .findAny();
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusList.values().stream().toList();
    }

    @Override
    public void delete(UUID statusUUID) {
        userStatusList.remove(statusUUID);
    }
}
