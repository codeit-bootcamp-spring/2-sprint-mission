package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserStatusRepository userStatusRepository;

  @Autowired
  private BinaryContentRepository binaryContentRepository;

  @Test
  @DisplayName("findByUsername - 존재하는 사용자")
  void testFindByUsernameExists() {
    // given
    User u = new User("alice", "alice@test.com", "pw", null);
    userRepository.save(u);

    // when
    Optional<User> found = userRepository.findByUsername("alice");

    // then
    assertThat(found).isPresent()
        .get()
        .extracting(User::getEmail)
        .isEqualTo("alice@test.com");
  }

  @Test
  @DisplayName("findByUsername - 없는 사용자")
  void testFindByUsernameNotExists() {
    // when
    Optional<User> found = userRepository.findByUsername("noone");

    // then
    assertThat(found).isEmpty();
  }

  @Test
  @DisplayName("existsByEmail / existsByUsername")
  void testExistsChecks() {
    // given
    User u = new User("bob", "bob@test.com", "pw", null);
    userRepository.save(u);

    // then
    assertThat(userRepository.existsByEmail("bob@test.com")).isTrue();
    assertThat(userRepository.existsByUsername("bob")).isTrue();
    assertThat(userRepository.existsByEmail("x@test.com")).isFalse();
    assertThat(userRepository.existsByUsername("x")).isFalse();
  }

  @Test
  @DisplayName("findAllWithProfileAndStatus - fetch join 동작")
  void testFindAllWithProfileAndStatus() {
    User u = new User("carol", "carol@test.com", "pw", null);
    userRepository.save(u);

    UserStatus status = new UserStatus(u, u.getCreatedAt());
    userStatusRepository.save(status);

    BinaryContent bc = new BinaryContent("f.png", 10L, "image/png");
    binaryContentRepository.save(bc);

    u.update(u.getUsername(), u.getEmail(), u.getPassword(), bc);
    userRepository.save(u);

    List<User> list = userRepository.findAllWithProfileAndStatus();

    assertThat(list).hasSize(1);
    User fetched = list.get(0);
    assertThat(fetched.getStatus()).isNotNull();
    assertThat(fetched.getProfile()).isNotNull();
    assertThat(fetched.getProfile().getFileName()).isEqualTo("f.png");
  }
}
