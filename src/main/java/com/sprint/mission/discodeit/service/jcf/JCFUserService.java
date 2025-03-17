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
    public User create(String userName, String email, String password) {
        User newUser = new User(userName, email, password);
        userData.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User find(UUID userId) {
        User userNullable = userData.get(userId);

        return Optional.ofNullable(userNullable)
        .orElseThrow(() -> new NoSuchElementException("유저 " + userId + "가 존재하지 않습니다."));
    }

    @Override
    public List<User> findAll() {
        return userData.values().stream().toList();
    }

    @Override
    public User update(UUID userId, String newUserName, String newEmail, String newPassword) {
        User userNullable = userData.get(userId);
        User user = Optional.ofNullable(userNullable).orElseThrow(() -> new NoSuchElementException(userId + "가 존재하지 않습니다."));
        user.updateUser(newUserName, newEmail, newPassword);

        return user;
    }

    @Override
    public void delete(UUID userId) {
        if (!userData.containsKey(userId)) {
            throw new NoSuchElementException("유저 " + userId + "가 존재하지 않습니다.");
        }
        userData.remove(userId);
    }

}
