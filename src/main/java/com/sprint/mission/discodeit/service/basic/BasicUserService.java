package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String name) {
        User user = new User(name);
        userRepository.save(user);
        return user;
    }

    @Override
    public Optional<User> getUserById(UUID userId) {
        return userRepository.getUserById(userId);
    }

    @Override
    public List<User> getUsersByName(String name) {
        return userRepository.getAllUsers().stream()
                .filter(user -> user.getName().equals(name))
                .toList();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public void updateUser(UUID userId, String newName) {
        userRepository.getUserById(userId).ifPresent(user -> {
            long updatedTime = System.currentTimeMillis();
            user.update(newName, updatedTime);
            userRepository.save(user);
        });
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.deleteUser(userId);
    }
}
