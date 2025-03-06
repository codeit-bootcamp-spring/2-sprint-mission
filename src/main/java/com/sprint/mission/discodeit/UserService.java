package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserService {
    void create(User user);
    User findById(UUID id);
    List<User> findAll();
    void update(UUID id, String username, String email) throws IOException;
    void delete(UUID id);
}
