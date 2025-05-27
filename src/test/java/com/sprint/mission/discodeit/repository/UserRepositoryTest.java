package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.domain.UserStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TestEntityManager entityManager;

  /**
   * TestFixture: 사용자 생성
   */
  private User createTestUser(String username, String email) {
    BinaryContent profile = BinaryContent.create("profile.jpg", 1024L, "image/jpeg");
    User user = User.create(username, email, "password123!@#", profile);
    // UserStatus 생성 및 연결
    UserStatus status = UserStatus.create(user, Instant.now());
    return user;
  }

  @Test
  @DisplayName("findByUsername_성공")
  void findByUsername_ExistingUsername_ShouldReturnUser() {
    // given
    String username = "testUser";
    User user = createTestUser(username, "test@example.com");
    userRepository.save(user);

    entityManager.flush();
    entityManager.clear();

    // when
    Optional<User> foundUser = userRepository.findByUsername(username);

    // then
    assertThat(foundUser).isPresent();
    assertThat(foundUser.get().getUsername()).isEqualTo(username);
  }

  @Test
  @DisplayName("findByUsername_빈 값 반환_존재하지 않는 사용자 이름")
  void findByUsername_NonExistingUsername_ShouldReturnEmptyOptional() {
    // given
    String nonExistingUsername = "nonExistingUser";

    // when
    Optional<User> foundUser = userRepository.findByUsername(nonExistingUsername);

    // then
    assertThat(foundUser).isEmpty();
  }

  @Test
  @DisplayName("findByEmail_성공")
  void findByEmail_WithExistingEmail_ShouldReturnMember() {
    // given
    String email = "bbori@dog.com";

    User user = createTestUser("강아지", email);
    userRepository.save(user);

    entityManager.flush();
    entityManager.clear();

    // when
    Optional<User> found = userRepository.findByEmail(email);

    // then
    assertThat(found).isPresent();
    assertThat(found.get().getUsername()).isEqualTo("강아지");
  }

  @Test
  @DisplayName("findByEmail_빈 값 반환_존재하지 않는 이메일")
  void findByEmail_WithNonExistingEmail_ShouldReturnEmpty() {
    // given
    String nonExistingEmail = "bbori@cat.com";

    // when
    Optional<User> found = userRepository.findByEmail(nonExistingEmail);

    // then
    assertThat(found).isEmpty();
  }

  @Test
  @DisplayName("findAll_성공_프로필 및 상태 정보와 함께 조회")
  void findAll_ReturnsUsersWithProfileAndStatus() {
    // given
    User user1 = createTestUser("user1", "user1@example.com");
    User user2 = createTestUser("user2", "user2@example.com");

    userRepository.saveAll(List.of(user1, user2));

    entityManager.flush();
    entityManager.clear();

    // when
    List<User> users = userRepository.findAll();

    // then
    assertThat(users).hasSize(2);
    assertThat(users).extracting("username").containsExactlyInAnyOrder("user1", "user2");
    
    User foundUser1 = users.stream().filter(u -> u.getUsername().equals("user1")).findFirst()
        .orElseThrow();
    User foundUser2 = users.stream().filter(u -> u.getUsername().equals("user2")).findFirst()
        .orElseThrow();

    // 프록시 초기화 여부 확인(프록시 초기화 없이도 접근 가능한지 확인)
    assertThat(Hibernate.isInitialized(foundUser1.getProfile())).isTrue();
    assertThat(Hibernate.isInitialized(foundUser1.getStatus())).isTrue();
    assertThat(Hibernate.isInitialized(foundUser2.getProfile())).isTrue();
    assertThat(Hibernate.isInitialized(foundUser2.getStatus())).isTrue();
  }

}
