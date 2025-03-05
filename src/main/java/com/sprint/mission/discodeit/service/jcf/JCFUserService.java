package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final Map<UUID, User> userMap;

    public JCFUserService() {
        this.userMap = new HashMap<>();
    }

    @Override
    public User create(String userName, String userEmail, String userPassword) {
        User user = new User(userName, userEmail, userPassword);
        this.userMap.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User findById(UUID userId) {
        User userNullable = this.userMap.get(userId);
        return Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다: " + userId));
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        User userNullable = this.userMap.get(userId);
        User user = Optional.ofNullable(userNullable)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다: " + userId));
        user.update(newUsername, newEmail, newPassword);
        return user;
    }


    @Override
    public void delete(UUID userId) {
        User removedUser = this.userMap.remove(userId);
        if (removedUser == null) {
            throw new NoSuchElementException("해당 사용자를 찾을 수 없습니다 : " + userId);
        }
    }
}