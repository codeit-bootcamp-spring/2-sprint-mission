package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.sprint.mission.discodeit.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TestEntityManager entityManager;

  @Test
  void findByUsername_success() {
    User user = new User("user", "user@test.com", "1234", null);
    entityManager.persist(user);
    entityManager.flush();

    Optional<User> found = userRepository.findByUsername(user.getUsername());

    assertThat(found).isPresent();
    assertThat(found.get().getUsername()).isEqualTo(user.getUsername());
  }

  @Test
  void findByUsername_fail() {
    Optional<User> found = userRepository.findByUsername("nonexistent");
    assertThat(found).isNotPresent();
  }

  @Test
  void existsByEmail_success() {
    User user = new User("user", "user@test.com", "1234", null);
    entityManager.persist(user);
    entityManager.flush();

    Boolean exists = userRepository.existsByEmail(user.getEmail());
    assertThat(exists).isTrue();
  }
}