package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final Map<UUID, User> data;

    public JCFUserService() {
        data = new HashMap<UUID, User>();
    }

    @Override
    public void createUser(String username) {
        User user = new User(username);
        data.put(user.getId(), user);
    }

    @Override
    public User getUserById(UUID id) {
        return data.get(id);
    }

    @Override
    public User getUserByName(String username) {
        for (User user : data.values()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<User>(data.values());
    }

    @Override
    public User updateUserName(UUID id, String username) {
        User user = data.get(id).updateUsername(username);
        data.put(id, user);
        return user;
    }

    @Override
    public void deleteUser(UUID id) {
        data.remove(id);

    }

    public boolean isDeleted(UUID id) {
        return data.containsKey(id);
    }
}
