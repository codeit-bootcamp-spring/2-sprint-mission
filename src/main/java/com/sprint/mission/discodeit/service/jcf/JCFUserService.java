package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {
    private final UserRepository userRepository = new JCFUserRepository();
    // UUID를 키로 사용하여 User 객체를 저장 / 일반적으로 키는 String UUID Integer 타입(변하지 않는 값)

    @Override
    public void create(User entity) {
        userRepository.save(entity);
    }

    @Override
    public Optional<User> read(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> readAll() {
        return userRepository.findAll();
    }

    @Override
    public void update(UUID id, User entity) {
        userRepository.update(id, entity);
    }

    @Override
    public void delete(UUID id) {
        userRepository.delete(id);
    }
}
