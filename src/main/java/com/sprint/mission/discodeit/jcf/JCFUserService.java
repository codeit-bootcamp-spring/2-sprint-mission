package com.sprint.mission.discodeit.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JCFUserService implements UserService {
    Map<String, User> users = new HashMap<String, User>();

    @Override
    public void createUser(String username) {
        users.put(username, new User(username));
    }

    @Override
    public User getUser(String username) {
        return users.get(username);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<User>(users.values());
    }

    @Override
    public void updateUsername(User user, String newUsername) {
        users.remove(user.getUsername());
        user.updateUsername(newUsername);
        users.put(newUsername, user);
    }

    @Override
    public void addChannel(User user, String channel) {
        user.updateJoinedChannel(channel);
        users.put(user.getUsername(), user);
    }

    @Override
    public void deleteUser(User user) {
        users.remove(user.getUsername());
    }

    @Override
    public void deleteChannel(User user, String channel) {
        user.updateJoinedChannel(channel);
        users.put(user.getUsername(), user);
    }
}
