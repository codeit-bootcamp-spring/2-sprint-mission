package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> users = new HashMap<>();

    public JCFUserService() {}

    @Override
    public void create(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public User find(UUID id) {
        return users.getOrDefault(id, null);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void update(User updateUser) {
        users.put(updateUser.getId(), updateUser);
    }

    @Override
    public void delete(UUID id) {
        users.remove(id);
    }
}
