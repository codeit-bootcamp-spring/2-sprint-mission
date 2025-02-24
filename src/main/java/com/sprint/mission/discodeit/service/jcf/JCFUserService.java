package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID,User> userData;

    public JCFUserService() {
        this.userData = new HashMap<>();
    }

    @Override
    public void create(String userName) {
        User user = new User(userName);
        userData.put(user.getId(), user);
    }

    @Override
    public void delete(String userName) {
        User deletedUser = read(userName);
        userData.remove(deletedUser.getId());
    }

    @Override
    public void update(String userName) {
        User user = read(userName);
        user.updateUser(userName);
    }

    @Override
    public User read(String userName) {
        return userData.values().stream()
                .filter(user -> user.getUserName().equals(userName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Map<UUID,User> readAll() {
        return userData;
    }

    @Override
    public String toString() {
        return "JCFUserService{" +
                "userData=" + userData +
                '}';
    }
}
