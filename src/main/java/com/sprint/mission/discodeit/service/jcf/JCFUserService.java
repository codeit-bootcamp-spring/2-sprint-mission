package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private static JCFUserService instance;

    private final Map<UUID, User> users;

    private JCFUserService() {
        users = new HashMap<>();
    }

    public static JCFUserService getInstance() {
        if (instance == null) {
            instance = new JCFUserService();
        }
        return instance;
    }

    @Override
    public User createUser(String nickname, String email, String avatar, String status) {
        if(getUserByEmail(email) != null) return null;

        User user = new User(nickname, email, avatar, status);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(UUID userId) {
        return users.get(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        return users.values().stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(UUID userId, String nickname, String avatar, String status) {
        User user = users.get(userId);
        user.update(nickname, avatar, status);
        return user;
    }

    @Override
    public boolean deleteUserByEmail(String email) {
        User user = users.values().stream()
                .filter(u -> email.equals(u.getEmail()))
                .findFirst()
                .orElse(null);

        if (user == null) return false;

        users.remove(user.getId());
        return true;
    }

    @Override
    public boolean deleteUserById(UUID userId) {
        User user = users.get(userId);
        if (user == null) return false;

        users.remove(user.getId());
        return true;
    }
}
