package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> data = new HashMap<>();

    public JCFUserStatusRepository() {}

    @Override
    public void save(UserStatus userStatus) {
        data.put(userStatus.getUserId(), userStatus);
    }

    @Override
    public Optional<UserStatus> getById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<UserStatus> getAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }
}
