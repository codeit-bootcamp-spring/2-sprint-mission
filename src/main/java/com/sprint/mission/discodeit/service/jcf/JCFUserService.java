package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> userDb = new HashMap<>();

    public JCFUserService() {}

    @Override
    public void create(User user) {
        userDb.put(user.getId(), user);
    }

    @Override
    public User find(UUID id) {
        return userDb.getOrDefault(id, null);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userDb.values());
    }

    @Override
    public void update(User updateUser) {
        userDb.put(updateUser.getId(), updateUser);
    }

    @Override
    public void delete(UUID id) {
        userDb.remove(id);
    }
}
