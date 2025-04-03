package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity._User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFUserRepository implements UserRepository {

  private final Map<UUID, _User> data;

  public JCFUserRepository() {
    this.data = new HashMap<>();
  }

  @Override
  public _User save(_User user) {
    this.data.put(user.getId(), user);
    return user;
  }

  @Override
  public Optional<_User> findById(UUID id) {
    return Optional.ofNullable(this.data.get(id));
  }

  @Override
  public Optional<_User> findByUsername(String username) {
    return this.findAll().stream()
        .filter(user -> user.getUsername().equals(username))
        .findFirst();
  }

  @Override
  public List<_User> findAll() {
    return this.data.values().stream().toList();
  }

  @Override
  public boolean existsById(UUID id) {
    return this.data.containsKey(id);
  }

  @Override
  public void deleteById(UUID id) {
    this.data.remove(id);
  }

  @Override
  public boolean existsByEmail(String email) {
    return this.findAll().stream().anyMatch(user -> user.getEmail().equals(email));
  }

  @Override
  public boolean existsByUsername(String username) {
    return this.findAll().stream().anyMatch(user -> user.getUsername().equals(username));
  }
}
