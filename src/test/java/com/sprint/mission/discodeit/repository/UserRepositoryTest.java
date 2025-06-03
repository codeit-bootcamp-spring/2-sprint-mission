package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.config.TestAuditingConfig;
import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.repository.JpaUserRepository;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(value = TestAuditingConfig.class)
public class UserRepositoryTest {

  @Autowired
  private JpaUserRepository userRepository;

  @Autowired
  private TestEntityManager entityManager;

  @Test
  void findAllWithProfileAndStatus() {
    // given
    User user1 = User.create("a", "a", "test", null);
    user1.setUserStatus(UserStatus.create(user1, Instant.now()));

    User user2 = User.create("b", "b", "test", null);
    user2.setUserStatus(UserStatus.create(user2, Instant.now()));

    User user3 = User.create("c", "c", "test", null);
    user3.setUserStatus(UserStatus.create(user3, Instant.now()));

    User user4 = User.create("d", "d", "test", null);
    user4.setUserStatus(UserStatus.create(user4, Instant.now()));

    User user5 = User.create("e", "e", "test", null);
    user5.setUserStatus(UserStatus.create(user5, Instant.now()));

    userRepository.saveAll(List.of(user1, user2, user3, user4, user5));
    entityManager.flush();
    entityManager.clear();

    // when
    List<User> userList = userRepository.findAllWithProfileAndStatus();

    // then
    assertThat(userList).hasSize(5);
    assertThat(userList).extracting(User::getName)
        .containsExactlyInAnyOrder("a", "b", "c", "d", "e");
    assertThat(userList).extracting(User::getEmail)
        .containsExactlyInAnyOrder("a", "b", "c", "d", "e");
    assertThat(userList).extracting(User::getPassword)
        .containsExactlyInAnyOrder("test", "test", "test", "test", "test");
  }

  @Test
  void findAllWithProfileAndStatus_whenUserHasNoStatus_shouldNotReturnThatUser() {
    // given
    User withStatus = User.create("withStatus", "email1@test.com", "pwd", null);
    withStatus.setUserStatus(UserStatus.create(withStatus, Instant.now()));

    User noStatus = User.create("noStatus", "email2@test.com", "pwd", null);

    userRepository.save(withStatus);
    userRepository.save(noStatus);

    entityManager.flush();
    entityManager.clear();
    // when
    List<User> userList = userRepository.findAllWithProfileAndStatus();

    // then
    assertThat(userList).hasSize(1);
    assertThat(userList)
        .extracting(User::getName)
        .contains("withStatus")
        .doesNotContain("noStatus");
  }
}
