package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final List<User> userList;

    public JCFUserService() {
        this.userList = new ArrayList<>();
    }

    @Override
    public User creatUser(User user) {
        boolean userExists = userList.stream()
                .anyMatch(existingUser -> existingUser.getUserName().equals(user.getUserName()));
        if (userExists) {
            throw new IllegalArgumentException("사용자 이름이 이미 존재합니다.");
        }
        userExists = userList.stream()
                .anyMatch(existingUser -> existingUser.getUserEmail().equals(user.getUserEmail()));
        if (userExists) {
            throw new IllegalArgumentException("해당 이메일이 이미 존재합니다.");
        }
        userList.add(user);
        return user;
    }

    @Override
    public List<User> findAllUser() {
        return userList;
    }

    @Override
    public User findByUserId(UUID userId) {
        return userList.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("사용자 아이디를 찾을 수 없습니다: " + userId));
    }

    @Override
    public User updateUser(UUID userId, User user) {
        return userList.stream()
                .filter(existingUser -> existingUser.getId().equals(userId))
                .findFirst()
                .map(existingUser -> {
                    if (user.getUserEmail() != null) {
                        existingUser.setUserEmail(user.getUserEmail());
                    }
                    if (user.getUserPassword() != null) {
                        existingUser.setUserPassword(user.getUserPassword());
                    }
                    return existingUser;
                })
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다: " + userId));
    }

    @Override
    public void deleteUser(UUID userId) {
        userList.removeIf(user -> user.getId().equals(userId));
    }
}