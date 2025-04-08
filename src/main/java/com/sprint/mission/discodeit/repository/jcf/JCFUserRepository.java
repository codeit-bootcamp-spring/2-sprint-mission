package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFUserRepository implements UserRepository {

  private final Map<UUID, User> data;

  public JCFUserRepository() {
    this.data = new HashMap<>();
  }

  @Override
  public User save(User user) {
    data.put(user.getId(), user);
    return user;
  }

  @Override
  public User update(User user, String newUsername, String newEmail, String newPassword,
      UUID newProfileId) {
    user.update(newUsername, newEmail, newPassword, newProfileId);

    return user;
  }

  @Override
  public List<User> findAll() {
    return this.data.values().stream().toList();
  }

  @Override
  public User findById(UUID userId) {
    return Optional.ofNullable(data.get(userId))
        .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
  }

  public Optional<User> findByUserName(String username) {
    return this.findAll().stream()
        .filter(user -> user.getUsername().equals(username))
        .findFirst();
  }

  @Override
  public void delete(UUID userId) {
    data.remove(userId);
  }
}
