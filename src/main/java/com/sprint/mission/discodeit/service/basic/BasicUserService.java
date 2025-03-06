package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class BasicUserService implements UserService {
    private static BasicUserService instance;
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static synchronized BasicUserService getInstance(UserRepository userRepository) {
        if (instance == null) {
        instance = new BasicUserService(userRepository);
        }
        return instance;
    }

    @Override
    public User create(String userName, String userEmail, String userPassword) {
        User user = new User(userName, userEmail, userPassword);
        userRepository.save(user);
        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public User findById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다: " + userId));
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다: " + userId));
        existingUser.update(newUsername, newEmail, newPassword);
        userRepository.save(existingUser);
        return existingUser;
    }

    @Override
    public void delete(UUID userId) {
        if (!userRepository.delete(userId)) {
            throw new NoSuchElementException("해당 사용자를 찾을 수 없습니다: " + userId);
        }
    }
}
