package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> users = new HashMap<>();

    @Override
    public User create(String username, String password, String email) {
        User user = new User(UUID.randomUUID(), username, password, email);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User find(UUID userId) {
        return users.get(userId);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User update(UUID userId, String newUsername, String newPassword, String newEmail) {
        User user = users.get(userId);
        if (user != null) {
            user.updateUsername(newUsername);
            user.setPassword(newPassword);
            user.setEmail(newEmail);
        }
        return user;
    }

    @Override
    public void delete(UUID authorId) {
        users.remove(authorId);
    }
}
