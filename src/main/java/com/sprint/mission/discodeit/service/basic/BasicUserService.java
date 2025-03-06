package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicUserService implements UserService {
    private static volatile BasicUserService instance;
    private final UserRepository userRepository;

    public static BasicUserService getInstance(UserRepository userRepository) {
        if (instance == null) {
            synchronized (BasicUserService.class) {
                if (instance == null) {
                    instance = new BasicUserService(userRepository);
                }
            }
        }
        return instance;
    }

    private BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(String username, String email, String password) {
        validateUserField(username, email, password);
        User user = new User(username, email, password);
        userRepository.save(user);
        return user;
    }

    @Override
    public User find(UUID userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new NoSuchElementException("유저가 존재하지 않습니다."));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        User user = find(userId);
        user.update(newUsername, newEmail, newPassword);
        userRepository.save(user);
        return user;
    }

    @Override
    public void delete(UUID userId) {
        find(userId); // 유저가 존재하는지 확인
        userRepository.deleteById(userId);
    }

    private void validateUserField(String username, String email, String password) {
        if (username == null || username.isBlank() || email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new IllegalArgumentException("username, email, password는 필수 입력값입니다.");
        }
    }

}
