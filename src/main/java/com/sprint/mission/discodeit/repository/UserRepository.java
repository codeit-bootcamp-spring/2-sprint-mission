package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    User save(User user);

    User update(User user, String newUsername, String newEmail, String newPassword, UUID newProfileId);

    List<User> findAll();

    User findById(UUID userId);

    void delete(UUID userId);
}
