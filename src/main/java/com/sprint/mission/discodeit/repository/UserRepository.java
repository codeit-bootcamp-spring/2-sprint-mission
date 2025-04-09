package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserRepository {

    void save(User user);

    User findByUserId(UUID userId);

    User findByUserName(String name);

    User findByEmail(String email);

    List<User> findAll();

    void delete(UUID userId);

    boolean existsByUsername(String username);

    boolean existByEmail(String email);

    boolean existById(UUID userId);
}
