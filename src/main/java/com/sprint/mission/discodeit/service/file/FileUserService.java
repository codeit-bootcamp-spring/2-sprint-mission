package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.UUID;

public class FileUserService implements UserService {
    private final UserRepository userRepository;

    public FileUserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void create(String name) {
        User user = new User(name);
        userRepository.save(user);
    }

    @Override
    public User findByUserId(UUID userId) {
        return userRepository.findByUserId(userId);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void update(UUID userId, String userName) {
        userRepository.update(userId, userName);
    }

    @Override
    public void remove(UUID userId) {
        userRepository.delete(userId);
    }
}
