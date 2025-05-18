package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.User;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@EntityScan(basePackages = "com.sprint.mission.discodeit.entity")
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @DisplayName("사용자 ID로 조회 - 성공")
  @Test
  void findByIdSuccess() {
    // given
    User user = new User(
        "test",
        "test@test.com",
        "password",
        null,
        null
    );
    userRepository.save(user);

    // when
    Optional<User> result = userRepository.findWithDetailsById(user.getId());

    // then
    assertThat(result).isPresent();
    assertThat(result.get().getUsername()).isEqualTo("test");
  }

  @DisplayName("사용자 ID로 조회 - 없는 ID로 실패")
  @Test
  void findByIdNotFound() {
    // given
    // when
    Optional<User> result = userRepository.findById(UUID.randomUUID());

    // then
    assertThat(result).isNotPresent();
  }

  @DisplayName("사용자 전체 조회 - 성공")
  @Test
  void findAllSuccess() {
    // given
    User user = new User(
        "test",
        "test@test.com",
        "password",
        null,
        null
    );
    userRepository.save(user);

    // when
    List<User> result = userRepository.findAllWithDetails();

    // then
    assertThat(result).isNotEmpty();
    assertThat(result.get(0).getUsername()).isEqualTo("test");
  }

  @DisplayName("사용자 전체 조회 - 유저 없음 실패")
  @Test
  void findAllNotFound() {
    // given
    // when
    List<User> result = userRepository.findAllWithDetails();

    // then
    assertThat(result).isEmpty();
  }

  @DisplayName("사용자 이름으로 조회 - 성공")
  @Test
  void findUsernameSuccess() {
    // given
    User user = new User(
        "test",
        "test@test.com",
        "password",
        null,
        null
    );
    userRepository.save(user);

    // when
    Optional<User> result = userRepository.findByUsername("test");

    // then
    assertThat(result).isPresent();
    assertThat(result.get().getUsername()).isEqualTo("test");
  }

  @DisplayName("사용자 이름으로 조회 - 없는 이름 실패")
  @Test
  void findUsernameNotFound() {
    // given
    // when
    Optional<User> result = userRepository.findByUsername("test");
    // then
    assertThat(result).isPresent();
  }

  @DisplayName("사용자 ID여러개로 조회 - 성공")
  @Test
  void findByIdsSuccess() {
    // given
    User user1 = new User(
        "test1",
        "test1@test.com",
        "password",
        null,
        null
    );
    userRepository.save(user1);

    User user2 = new User(
        "test2",
        "test2@test.com",
        "password",
        null,
        null
    );
    userRepository.save(user2);

    Set<UUID> ids = new HashSet<>();
    ids.add(user1.getId());
    ids.add(user2.getId());

    // when
    List<User> result = userRepository.findByIdIn(ids);

    // then
    assertThat(result).isNotEmpty();
    assertThat(result).extracting("username")
        .containsExactlyInAnyOrder("test1", "test2");
  }

  @DisplayName("사용자 ID여러개로 조회 - 유저 없음 실패")
  @Test
  void findByIdsNotFound() {
    // given
    // when
    List<User> result = userRepository.findByIdIn(new HashSet<>());
    // then
    assertThat(result).isEmpty();
  }

  @DisplayName("사용자 이름 존재 확인 - 성공")
  @Test
  void existsByUsernameSuccess() {
    // given
    User user = new User("test", "test@test.com", "pass", null, null);
    userRepository.save(user);

    // when
    boolean exists = userRepository.existsByUsername("test");

    // then
    assertThat(exists).isTrue();
  }

  @DisplayName("사용자 이름 존재 확인 - 존재하지 않는 이름 실패")
  @Test
  void existsByUsernameNotFound() {
    // given
    // when
    boolean exists = userRepository.existsByUsername("test");

    // then
    assertThat(exists).isFalse();
  }

  @DisplayName("사용자 이메일 존재 확인 - 성공")
  @Test
  void existsByEmailSuccess() {
    // given
    User user = new User("test", "test@test.com", "pass", null, null);
    userRepository.save(user);

    // when
    boolean exists = userRepository.existsByEmail("test@test.com");

    // then
    assertThat(exists).isTrue();
  }

  @DisplayName("사용자 이메일 존재 확인 - 존재하지 않는 이메일 실패")
  @Test
  void existsByEmailNotFound() {
    // given
    // when
    boolean exists = userRepository.existsByEmail("test@test.com");

    // then
    assertThat(exists).isFalse();
  }
}
