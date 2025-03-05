package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class FileUserService implements UserService {
    private UserRepository userRepository;

    public FileUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(String username, String email, String password) {
        User user = new User(username, email, password);
        return userRepository.save(user);
    }

    @Override
    public User find(UUID userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        user.update(newUsername, newEmail, newPassword);
        return userRepository.save(user);
    }

    @Override
    public void delete(UUID userId) {
        if (!userRepository.exists(userId)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        userRepository.delete(userId);
    }

    @Override
    public boolean exists(UUID authorId) {
        return userRepository.exists(authorId);
    }
}
