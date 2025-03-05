package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Map;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final UserRepository jcfUserRepository;

    public JCFUserService(UserRepository jcfUserRepository) {
        this.jcfUserRepository = jcfUserRepository;
    }

    @Override
    public void createUser(String userName, String userEmail, String password) {
        User newUser = new User(userName, userEmail, password); //각 요소에 대한 유효성 검증은 User 생성자에게 맡긴다
        this.jcfUserRepository.add(newUser);
    }

    @Override
    public User readUser(UUID userId) {
        return jcfUserRepository.findById(userId);
    }

    @Override
    public Map<UUID, User> readAllUsers() {
        return jcfUserRepository.getAll();
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
        jcfUserRepository.deleteById(userId);
    }
}
