package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final UserRepository userRepository;

    public JCFUserService(UserRepository userRepository) {
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
        userRepository.modify(userId, userName);
    }

    @Override
    public User remove(UUID userId) {
        return userRepository.delete(userId);
    }
}
