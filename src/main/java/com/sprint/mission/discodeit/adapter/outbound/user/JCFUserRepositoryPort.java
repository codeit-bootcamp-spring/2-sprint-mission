package com.sprint.mission.discodeit.adapter.outbound.user;

import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFUserRepositoryPort implements UserRepositoryPort {

  private Map<UUID, User> userList = new ConcurrentHashMap<>();

  @Override
  public User save(User user) {
    userList.put(user.getId(), user);
    return user;
  }

  @Override
  public Optional<User> findById(UUID userId) {
    return Optional.ofNullable(userList.get(userId));
  }

  @Override
  public List<User> findAll() {
    return userList.values().stream().toList();
  }

  @Override
  public void delete(UUID id) {
    userList.remove(id);
  }

  @Override
  public boolean existId(UUID id) {
    return userList.values().stream().anyMatch(u -> u.getId().equals(id));
  }

  @Override
  public boolean existName(String name) {
    return userList.values().stream().anyMatch(u -> u.getName().equalsIgnoreCase(name));
  }

  @Override
  public boolean existEmail(String email) {
    return userList.values().stream().anyMatch(u -> u.getEmail().equals(email));
  }
}
