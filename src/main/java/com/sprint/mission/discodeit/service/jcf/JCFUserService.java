package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final UserRepository userRepository;

    public JCFUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User createUser(String username) {
        User user = new User(username);
        userRepository.save(user);
        return user;
    }

    @Override
    public User getUser(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(UUID id, String newUsername) {
        User user = userRepository.findById(id);
        if (user != null) {
            user.updateUsername(newUsername);
            userRepository.save(user);
        }
    }

    @Override
    public void deleteUser(UUID id) {
        userRepository.delete(id);
    }
}
