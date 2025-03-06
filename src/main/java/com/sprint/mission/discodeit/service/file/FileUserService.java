package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Map;
import java.util.UUID;

public class FileUserService implements UserService {
    private final UserRepository fileUserRepository;

    public FileUserService(UserRepository fileUserRepository) {
        this.fileUserRepository = fileUserRepository;
    }

    @Override
    public void createUser(String userName, String userEmail, String password) {
        User newUser = new User(userName, userEmail, password); //각 요소에 대한 유효성 검증은 User 생성자에게 맡긴다
        this.fileUserRepository.add(newUser);
    }

    @Override
    public User readUser(UUID userId) {
        return fileUserRepository.findById(userId);
    }

    @Override
    public Map<UUID, User> readAllUsers() {
        return fileUserRepository.getAll();
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
        fileUserRepository.deleteById(userId);
    }
}
