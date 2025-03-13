package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    User findByKey(UUID userKey);
    User findByUserId(String userId);
    boolean existsByKey(UUID userKey);
    void delete(User user);
    List<User> findAllByIds(List<String> userIds);
}
