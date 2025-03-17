package com.sprint.discodeit.service.file;

import com.sprint.discodeit.entity.User;
import com.sprint.discodeit.repository.file.FileUserRepository;
import com.sprint.discodeit.service.UserService;
import java.util.List;
import java.util.UUID;

public class FileUserService implements UserService {

    private final FileUserRepository fileUserRepository;

    public FileUserService(FileUserRepository fileUserRepository) {
        this.fileUserRepository = fileUserRepository;
    }


    @Override
    public User create(String username, String email, String password) {
        User user = new User(username, email, password);
        fileUserRepository.save(user);
        return user;
    }

    @Override
    public User find(UUID userId) {
        User byId = fileUserRepository.findById(userId.toString());
        return byId;
    }

    @Override
    public List<User> findAll() {
        return fileUserRepository.findByAll();
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        User user = fileUserRepository.findById(userId.toString());
        user.update(newUsername, newEmail, newPassword);
        fileUserRepository.save(user);
        return user;
    }

    @Override
    public void delete(UUID userId) {
        fileUserRepository.delete(userId);
    }
}
