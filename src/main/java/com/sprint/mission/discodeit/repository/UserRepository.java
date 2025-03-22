package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Optional;

public interface UserRepository {

    Map<UUID, User> getUserData();

    User save(User user);

    User update(User user, String newUsername, String newEmail, String newPassword, UUID newProfileId);

    List<User> findAll();

    User findById(UUID userId);

    void delete(UUID userId);
}
