package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFUserService implements UserService {
    private final List<User> userList;

    public JCFUserService() {
        this.userList = new ArrayList<>();
    }

    @Override
    public User create(User user) {
        boolean userExists = userList.stream()
                .anyMatch(existingUser -> existingUser.getUserId().equals(user.getUserId()));
        if (userExists) {
            throw new IllegalArgumentException("사용자 ID가 이미 존재합니다.");
        }
        userExists = userList.stream()
                .anyMatch(existingUser -> existingUser.getUserName().equals(user.getUserName()));
        if (userExists) {
            throw new IllegalArgumentException("사용자 이름이 이미 존재합니다.");
        }
        userExists = userList.stream()
                .anyMatch(existingUser -> existingUser.getUserEmail().equals(user.getUserEmail()));
        if (userExists) {
            throw new IllegalArgumentException("해당 이메일이 이미 존재합니다.");
        }
        this.userList.add(user);
        return user;
    }

    @Override
    public User find(UUID id) {
        return userList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("사용자 아이디를 찾을 수 없습니다: " + id));
    }

    @Override
    public List<User> findAll() {
        return userList.stream().collect(Collectors.toList());
    }

    @Override
    public User update(UUID id, User user) {
        return userList.stream()
                .filter(existingUser -> existingUser.getId().equals(id))
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
                .orElseThrow(() -> new IllegalArgumentException("사용자 아이디를 찾을 수 없습니다: " + id));
    }

    @Override
    public void delete(UUID id) {
        userList.removeIf(user -> user.getId().equals(id));
    }
}