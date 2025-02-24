package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Set;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final UserRepository userRepository;

    public JCFUserService() {
        this.userRepository = new UserRepository();
    }

    @Override
    public void createUser(User newUser) {
        this.userRepository.addUser(newUser);
    }

    @Override
    public User readUser(UUID userId) {
        validateUserId(userId);
        return userRepository.findUserById(userId);
    }

    @Override
    public Set<User> readAllUsers() {
        return userRepository.getUsers();
    }

    @Override
    public void updateUserName(UUID userId, String newUserName) {
        readUser(userId).updateUserName(newUserName);
    }

    @Override
    public void updatePassword(UUID userId, String newPassword) {
        readUser(userId).updateUserPassword(newPassword);
    }

    @Override
    public void deleteUser(UUID userId) {
        validateUserId(userId);
        userRepository.deleteUser(userId);
    }

    public void validateUserId(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("null 인 id 값이 들어왔습니다!!!");
        }
    }
}
