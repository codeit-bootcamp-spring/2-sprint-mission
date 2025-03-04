package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {
    private final UserRepository userRepository = new FileUserRepository();

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
