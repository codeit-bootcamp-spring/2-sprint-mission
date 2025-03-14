package com.sprint.mission.discodeit.service.basic;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;


import java.util.*;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void create(User user) {
        if (userRepository.findById(user.getId()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 ID입니다: " + user.getId());
        }

        userRepository.save(user);
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다." + id));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll().orElse(Collections.emptyList());
    }

    @Override
    public void update(UUID id, String username, String email) {
        User user = this.findById(id);

        user.setUpdatedAt(System.currentTimeMillis());
        user.setUsername(username);
        user.setEmail(email);

        userRepository.update(user);
    }

    @Override
    public void delete(UUID id) {
        this.findById(id);
        userRepository.delete(id);
    }
}
