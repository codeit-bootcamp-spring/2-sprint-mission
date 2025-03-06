package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class FileUserService implements UserService {
    private final UserRepository userRepository;

    public FileUserService() {
        this.userRepository = new FileUserRepository();
    }

    @Override
    public User createUser(String userName) {
        User user = new User(userName);
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
        userRepository.save(user);
        return user;
    }

    @Override
    public User getUser(UUID id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(UUID id, String newUsername) {
        User user = getUser(id);
        user.updateUsername(newUsername);
        userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID id) {
        getUser(id);
        userRepository.delete(id);
    }
}
