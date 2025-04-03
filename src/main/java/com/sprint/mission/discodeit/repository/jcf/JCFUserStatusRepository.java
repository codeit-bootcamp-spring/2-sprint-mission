package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    Map<UUID, UserStatus> data = new HashMap<>();

    @Override
    public UserStatus save(UserStatus userStatus) {
        return data.put(userStatus.getUserKey(), userStatus);
    }

    @Override
    public void delete(UUID userStatusKey) {
        data.remove(userStatusKey);
    }

    @Override
    public List<UserStatus> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public UserStatus findByUserKey(UUID userKey) {
        return data.values().stream()
                .filter(userStatus -> userStatus.getUserKey().equals(userKey))
                .findFirst()
                .orElse(null);
    }

    @Override
    public UserStatus findByKey(UUID userStatusKey) {
        return data.get(userStatusKey);
    }
}
