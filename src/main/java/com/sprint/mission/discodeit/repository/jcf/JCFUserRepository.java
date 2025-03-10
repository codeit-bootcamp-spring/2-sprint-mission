package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.entity.User;
import java.util.*;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> userStorage = new HashMap<>();

    @Override
    public void save(User user) {
        userStorage.put(user.getId(), user);
    }

    @Override
    public User findById(UUID id) {
        return userStorage.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userStorage.values());
    }
}
