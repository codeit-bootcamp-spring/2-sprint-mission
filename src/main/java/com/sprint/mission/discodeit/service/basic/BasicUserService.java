package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String userName, String userEmail, String password) {
        User newUser = new User(userName, userEmail, password); //각 요소에 대한 유효성 검증은 User 생성자에게 맡긴다
        this.userRepository.add(newUser);
        return newUser;
    }

    @Override
    public User readUser(UUID userId) {
        return this.userRepository.findById(userId);
    }

    @Override
    public Map<UUID, User> readAllUsers() {
        return this.userRepository.getAll();
    }

    @Override
    public void updateUserName(UUID userId, String newUserName) {
        this.userRepository.updateUserName(userId, newUserName);
    }

    @Override
    public void updatePassword(UUID userId, String newPassword) {
        this.userRepository.updatePassword(userId, newPassword);
    }

    @Override
    public void deleteUser(UUID userId) {
        this.userRepository.deleteById(userId);
    }
}
