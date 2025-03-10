package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(String username, String password, String email) {
        User user = new User(UUID.randomUUID(), username, password, email);
        userRepository.save(user);
        return user;
    }

    @Override
    public User find(UUID userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
    public User update(UUID authorId, String newName, String newEmail, String newPassword){
        return userRepository.findById(authorId);
    }
    public void delete(UUID authorId){

    }
}
