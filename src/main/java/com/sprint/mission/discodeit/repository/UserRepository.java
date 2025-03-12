package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    User findByKey(UUID userKey);
    List<User> findAllByKeys(List<UUID> userKeyList);
    boolean existsByKey(UUID userKey);
    void delete(User user);
    String findUserId(UUID userKey);
    UUID findUserKeyById(String userId);
    List<UUID> findUserKeyByIds(List<String> userIds);
    String findUserName(UUID userKey);
}
