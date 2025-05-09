package com.sprint.mission.discodeit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
class UserRepositoryTest {

  @Autowired
  UserRepository userRepository;

  @Autowired
  UserStatusRepository userStatusRepository;

  @Test
  void findByUsername_성공() {
    User user = new User("john", "john@email.com", "1234", null);
    userRepository.save(user);

    Optional<User> found = userRepository.findByUsername("john");

    assertTrue(found.isPresent());
    assertEquals("john", found.get().getUsername());
  }

  @Test
  void existsByEmail_실패() {
    assertFalse(userRepository.existsByEmail("notexist@email.com"));
  }

  @Test
  void findAllWithProfileAndStatus_쿼리_확인() {

    User user = new User("jane", "jane@email.com", "1234", null);
    UserStatus status = userStatusRepository.save(new UserStatus(user, Instant.now()));
    userStatusRepository.save(status);
    userRepository.save(user);

    List<User> result = userRepository.findAllWithProfileAndStatus();

    assertEquals(1, result.size());
    assertNotNull(result.get(0).getStatus());
  }
}
