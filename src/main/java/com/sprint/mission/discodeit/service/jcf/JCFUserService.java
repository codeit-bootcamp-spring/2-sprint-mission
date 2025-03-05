package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    private final Map<UUID, User> data;

    public JCFUserService() {
        this.data = new HashMap<>();
    }

    @Override
    public User create(String username, String email, String password) {
        User user = new User(username, email, password);
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public User find(UUID userId) {
        return data.get(userId);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User update(UUID userId, String username, String email, String password) {
        User user = data.get(userId);
        if (user != null) {
            user.update(username, email, password);
        }
        return user;
    }

    @Override
    public void delete(UUID userId) {
        data.remove(userId);
    }
}


