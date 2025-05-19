package com.sprint.mission.discodeit.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
@EnableJpaAuditing
class UserRepositoryTest {
  @Autowired
  private UserRepository userRepository;

  @Test
  void findByUsername_success() {
    // given
    BinaryContent profile = new BinaryContent("profile.png", 3L, "image/png");
    User user = new User("user1", "user1@email.com", "password", profile);
    UserStatus status = new UserStatus(user, Instant.now());

    userRepository.save(user);

    // when
    Optional<User> found = userRepository.findByUsername(user.getUsername());

    // then
    assertTrue(found.isPresent());
    assertEquals(user.getUsername(), found.get().getUsername());
    assertEquals(user.getEmail(), found.get().getEmail());
    assertEquals(user.getPassword(), found.get().getPassword());
    assertEquals(user.getStatus(), found.get().getStatus());
  }
}