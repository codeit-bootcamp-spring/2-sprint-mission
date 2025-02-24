package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class JCFUserService implements UserService {
    private final List<User> userList;

    public JCFUserService() {
        this.userList = new ArrayList<>();
    }

    @Override
    public User create(User user) {
        for (User existingUser : userList) {
            if (existingUser.getUserId().equals(user.getUserId())) {
                throw new IllegalArgumentException("사용자 ID가 이미 존재합니다.");
            }
            if (existingUser.getUserEmail().equals(user.getUserEmail())) {
                throw new IllegalArgumentException("해당 이메일이 이미 존재합니다.");
            }
            if (existingUser.getUserName().equals(user.getUserName())) {
                throw new IllegalArgumentException("사용자 이름이 이미 존재합니다.");
            }
        }
        this.userList.add(user);
        return user;
    }

    @Override
    public User find(String identifier) {
        for (User user : userList) {
            if (user.getUserId().equals(identifier) || user.getUserName().equals(identifier) || user.getUserEmail().equals(identifier)) {
                return user;
            }
        }
        throw new IllegalArgumentException("사용자를 찾을 수 없습니다: " + identifier);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userList);
    }

    @Override
    public User update(String identifier, User user) {
        for (User existingUser : userList) {
            if (existingUser.getUserId().equals(identifier)) {
                if (user.getUserEmail() != null) {
                    existingUser.setUserEmail(user.getUserEmail());
                }
                if (user.getUserPassword() != null) {
                    existingUser.setUserPassword(user.getUserPassword());
                }
                return existingUser;
            }
        }
        return null;
    }

    @Override
    public void delete(String identifier) {
        userList.removeIf(user -> user.getUserId().equals(identifier) ||
                user.getUserName().equals(identifier) ||
                user.getUserEmail().equals(identifier));
    }
}
