package com.sprint.sprint2.discodeit.service.file;

import com.sprint.sprint2.discodeit.entity.User;
import com.sprint.sprint2.discodeit.service.UserService;
import java.util.List;
import java.util.UUID;

public class FileUserService implements UserService {


    @Override
    public User create(String username, String email, String password) {
        return null;
    }

    @Override
    public User find(UUID userId) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public User update(UUID userId, String newUsername, String newEmail, String newPassword) {
        return null;
    }

    @Override
    public void delete(UUID userId) {

    }
}
