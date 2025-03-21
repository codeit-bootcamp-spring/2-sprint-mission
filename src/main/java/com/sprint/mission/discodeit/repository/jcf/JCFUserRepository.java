package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> data = new HashMap<>();

    @Override
    public User save(User user) {
        data.put(user.getUuid(), user);

        return user;
    }

    @Override
    public User findByKey(UUID userKey) {
        return data.get(userKey);
    }

    @Override
    public boolean existsByKey(UUID userKey) {
        return data.containsKey(userKey);
    }

    @Override
    public boolean existsByName(String userName) {
        return data.values().stream().anyMatch(user -> user.getName().equals(userName));
    }

    @Override
    public boolean existsByEmail(String userEmail) {
        return data.values().stream().anyMatch(user -> user.getEmail().equals(userEmail));
    }

    @Override
    public void delete(User user) {
        data.remove(user.getUuid());
    }

    @Override
    public List<User> findAll() {
        return data.values().stream().toList();
    }
}
