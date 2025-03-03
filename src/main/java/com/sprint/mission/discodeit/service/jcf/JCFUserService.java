package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Set;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final UserRepository userRepository;

    public JCFUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(String userName, String userEmail, String password) {
        User newUser = new User(userName, userEmail, password); //각 요소에 대한 유효성 검증은 User 생성자에게 맡긴다
        this.userRepository.addUser(newUser);
    }

    @Override
    public User readUser(UUID userId) {
        UserService.validateUserId(userId, this.userRepository);        // 아래 코드와 중복되는 코드 존재.
        return userRepository.findUserById(userId);
    }

    @Override
    public Set<User> readAllUsers() {
        return userRepository.getUsers();
    }

    @Override
    public void updateUserName(UUID userId, String newUserName) {
        UserService.validateUserId(userId, this.userRepository);
        readUser(userId).updateUserName(newUserName);
    }

    @Override
    public void updatePassword(UUID userId, String newPassword) {
        UserService.validateUserId(userId, this.userRepository);
        readUser(userId).updateUserPassword(newPassword);
    }

    @Override
    public void deleteUser(UUID userId) {
        UserService.validateUserId(userId, this.userRepository);
        userRepository.deleteUser(userId);
    }
}
