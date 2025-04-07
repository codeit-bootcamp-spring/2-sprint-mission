package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.User;
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

  private final Map<UUID, User> users = new HashMap<>();

  @Override
  public User save(User user) {
    users.put(user.getId(), user);

    return user;
  }

  @Override
  public Optional<User> findByUserId(UUID id) {
    return Optional.ofNullable(users.get(id));
  }

  @Override
  public Optional<User> findByName(String name) {
    return users.values()
        .stream()
        .filter(user -> user.isSameName(name))
        .findFirst();
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return users.values()
        .stream()
        .filter(user -> user.isSameEmail(email))
        .findFirst();
  }

  @Override
  public List<User> findAll() {
    return users.values()
        .stream()
        .toList();
  }

  @Override
  public void delete(UUID id) {
    users.remove(id);
  }
}
