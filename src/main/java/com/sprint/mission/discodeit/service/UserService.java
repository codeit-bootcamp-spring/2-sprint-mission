package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserService {
    User create( String name, String email,String password);
    User find(UUID authorId);
    List<User> findAll();
    User update(UUID authorId, String newName, String newEmail, String newPassword);
    void delete(UUID authorId);
}
