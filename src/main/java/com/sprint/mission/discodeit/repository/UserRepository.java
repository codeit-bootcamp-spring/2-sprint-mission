package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    User findByKey(UUID userKey);
    List<User> findAll();
    boolean existsByKey(UUID userKey);
    boolean existsByName(String userName);
    boolean existsByEmail(String userEmail);
    void delete(User user);
}
