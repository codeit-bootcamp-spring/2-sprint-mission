package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;


public interface UserService {
    User create(String name, String email, String password);
    User getUser(UUID userId);
    List<User> getAllUser();
    User update(UUID userId, String changeName, String changeEmail, String changePassword);
    void delete(UUID userId);
}
