package com.sprint.mission.discodeit.infra;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserRepository {
    User save(User user);

    User findById(UUID id);

    List<User> findByName(String name);

    User findByEmail(String email);

    List<User> findAll();

    void updateName(UUID id, String name);

    void delete(UUID id);
}
