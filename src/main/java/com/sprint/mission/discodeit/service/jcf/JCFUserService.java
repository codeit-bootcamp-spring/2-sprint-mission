package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private volatile  static JCFUserService instance = null;
    private final Map<UUID, User> data;

    private JCFUserService() {
        this.data = new HashMap<>();
    }

    public static JCFUserService getInstance() {
        if (instance == null) {
            synchronized (JCFUserService.class) {
                if (instance == null)
                    instance = new JCFUserService();
            }
        }
        return instance;
    }

    @Override
    public User createUser(String name) {
        User user = new User(name);
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<User> getUsersByName(String name) {
        return data.values().stream()
                .filter(user -> user.getName().equals(name))
                .toList();
    }

    @Override
    public List<User> getAllUsers() {
        return data.values().stream().toList();
    }

    @Override
    public void updateUserName(UUID id, String name) {
        if (data.containsKey(id)) {
            data.get(id).updateName(name);
        }
    }

    @Override
    public void deleteUser(UUID id) {
            data.remove(id);
    }
}
