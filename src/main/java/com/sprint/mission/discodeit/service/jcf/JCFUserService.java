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
    public User create(String username, String password) {
        User user = new User(username, password);
        this.data.put(user.getId(), user);

        return user;
    }

    @Override
    public User findById(UUID userId) {
        User userNullable = this.data.get(userId);

        return Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    }

    @Override
    public List<User> findAll() {
        return this.data.values().stream().toList();
    }

    @Override
    public User updateName(UUID userId, String newUsername) {
        User userNullable = this.data.get(userId);
        User user = Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException(userId + " 존재하지 않는 유저입니다."));
        user.updateName(newUsername);

        return user;
    }

    @Override
    public User updatePassword(UUID userId, String newPassword) {
        User userNullable = this.data.get(userId);
        User user = Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException(userId + " 존재하지 않는 유저입니다."));
        user.updatePassword(newPassword);

        return user;
    }

    @Override
    public void delete(UUID userId) {
        if (!this.data.containsKey(userId)) {
            throw new NoSuchElementException(userId + " 삭제할 대상이 존재하지 않습니다.");
        }
        this.data.remove(userId);
    }
}
