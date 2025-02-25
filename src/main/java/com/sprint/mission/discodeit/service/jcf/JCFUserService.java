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
    public User createUser(User user) {
        data.put(user.getId(), user);
        return user;
    }

    @Override
    public User readUser(UUID id) {
        return data.get(id);
    }

    @Override
    public List<User> readAllUsers() {
        return new ArrayList<>(data.values());
    }

    @Override
    public User updateUser(UUID id, User user) {
        if(data.containsKey(id)) {
            User exisingUser = data.get(id);
            exisingUser.update(user.getName());
            return exisingUser;
        }
        return null;
    }

    @Override
    public void deleteUser(UUID id) {
        data.remove(id);
    }
}
