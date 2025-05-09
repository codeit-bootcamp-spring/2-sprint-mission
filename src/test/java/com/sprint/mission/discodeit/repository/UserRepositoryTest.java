package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserStatusRepository userStatusRepository;

  @Test
  @DisplayName("이메일 존재 여부 확인")
  void existsByEmail() {
    // given
    User user = new User("tester", "tester@example.com", "password", null);
    userRepository.save(user);

    // when
    boolean result = userRepository.existsByEmail("tester@example.com");

    // then
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("사용자 이름 존재 여부 확인")
  void existsByUsername() {
    // given
    User user = new User("uniqueUser", "unique@example.com", "password", null);
    userRepository.save(user);

    // when
    boolean result = userRepository.existsByUsername("uniqueUser");

    // then
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("프로필과 상태 정보를 포함한 사용자 전체 조회")
  void findAllWithProfileAndStatus() {
    // given
    User user = new User("tester", "tester2@example.com", "password", null);
    userRepository.save(user);

    UserStatus status = new UserStatus(user, user.getCreatedAt());
    userStatusRepository.save(status);

    // when
    var results = userRepository.findAllWithProfileAndStatus();

    // then
    assertThat(results).hasSize(1);
    assertThat(results.get(0).getUsername()).isEqualTo("tester");
    assertThat(results.get(0).getStatus()).isNotNull();
  }
}