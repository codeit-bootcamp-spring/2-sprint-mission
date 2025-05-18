package com.sprint.mission.discodeit;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  @DisplayName("이메일 존재 여부 확인 - 존재함")
  void existsByEmail_success() {
    User user = new User("user1", "test@example.com", "pass", null);
    userRepository.save(user);

    boolean exists = userRepository.existsByEmail("test@example.com");
    assertThat(exists).isTrue();
  }

  @Test
  @DisplayName("이메일 존재 여부 확인 - 존재하지 않음")
  void existsByEmail_failure() {
    boolean exists = userRepository.existsByEmail("unknown@example.com");
    assertThat(exists).isFalse();
  }

  @Test
  @DisplayName("사용자 이름으로 조회 - 성공")
  void findByUsername_success() {
    User user = new User("user2", "email2@example.com", "pass", null);
    userRepository.save(user);

    Optional<User> found = userRepository.findByUsername("user2");
    assertThat(found).isPresent();
  }

  @Test
  @DisplayName("사용자 이름으로 조회 - 실패")
  void findByUsername_failure() {
    Optional<User> found = userRepository.findByUsername("nonexistent");
    assertThat(found).isEmpty();
  }
}

