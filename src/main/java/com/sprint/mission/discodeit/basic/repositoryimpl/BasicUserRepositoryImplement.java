package com.sprint.mission.discodeit.basic.repositoryimpl;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserRepository;

import java.util.*;


public class BasicUserRepositoryImplement implements UserRepository {

  private final Map<UUID, User> userRepository = new HashMap<>();

  @Override
  public void saveData() {

  }

  @Override
  public Optional<User> findByUser(UUID userId) {
    return Optional.ofNullable(userRepository.get(userId));
  }

  @Override
  public void register(User user) {
    userRepository.put(user.getId(), user);
  }

  @Override
  public boolean deleteUser(UUID userId) {
    return userRepository.remove(userId) != null;
  }

  @Override
  public Set<UUID> findAllUsers() {
    return new HashSet<>(userRepository.keySet());
  }

  @Override
  public boolean updateUser(User user) {
    if (userRepository.containsKey(user.getId())) {
      userRepository.put(user.getId(), user);
      return true;
    }
    return false;
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return userRepository.values().stream()
        .filter(user -> user.getEmail() != null && user.getEmail().equals(email))
        .findFirst();
  }
}





