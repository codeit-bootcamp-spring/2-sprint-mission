package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.util.*;

public class JCFUserStatusRepository implements UserStatusRepository {
    private static final JCFUserStatusRepository instance = new JCFUserStatusRepository();
    private final Map<UUID, UserStatus> data = new HashMap<>();

    private JCFUserStatusRepository() {}

    public static JCFUserStatusRepository getInstance() {
        return instance;
    }

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
