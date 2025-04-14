package com.sprint.mission.discodeit.adapter.outbound.user;

import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.port.UserRepositoryPort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

  private final JpaUserRepository jpaUserRepository;


  @Override
  public User save(User user) {
    return jpaUserRepository.save(user);
  }

  @Override
  public Optional<User> findById(UUID id) {
    return jpaUserRepository.findById(id);
  }

  @Override
  public Optional<User> findByName(String name) {
    return jpaUserRepository.findByName(name);
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return jpaUserRepository.findByEmail(email);
  }

  @Override
  public List<User> findAll() {
    return jpaUserRepository.findAll();
  }

  @Override
  public void delete(UUID id) {
    jpaUserRepository.deleteById(id);
  }

  @Override
  public boolean existId(UUID id) {
    return jpaUserRepository.existsById(id);
  }
}
