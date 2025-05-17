package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TestEntityManager entityManager;

  @Test
  @DisplayName("사용자 프로필과 상태와 함께 모든 사용자 조회")
  public void testFindAllWithProfileAndStatus_Success() {
    BinaryContent profile = new BinaryContent("profile.jpg", 1024L, "image/jpeg");
    User user = new User("testuser", "testuser@example.com", "password123", profile);
    UserStatus userStatus = new UserStatus(user, Instant.now());
    userRepository.save(user);
    entityManager.persist(userStatus);
    entityManager.flush();
    entityManager.clear();

    var users = userRepository.findAllWithProfileAndStatus();

    assertThat(users).isNotEmpty();
    assertThat(users.get(0).getProfile()).isNotNull();
    assertThat(users.get(0).getStatus()).isNotNull();
  }

  @Test
  @DisplayName("사용자 프로필과 상태 등 사용자가 없는 경우 빈 값 반환")
  public void testFindAllWithProfileAndStatus_Empty() {
    var users = userRepository.findAllWithProfileAndStatus();

    assertThat(users).isEmpty();
  }

  @Test
  @DisplayName("페이징 및 정렬된 사용자 목록 조회")
  public void testFindUsersPagedAndSorted() {
    for (int i = 0; i < 10; i++) {
      BinaryContent profile = new BinaryContent("profile" + i + ".jpg", 1024L, "image/jpeg");
      User user = new User("user" + i, "user" + i + "@example.com", "password123", profile);
      UserStatus status = new UserStatus(user, Instant.now());
      userRepository.save(user);
      entityManager.persist(status);
    }

    entityManager.flush();
    entityManager.clear();

    var pageRequest = PageRequest.of(0, 5, Sort.by("username").descending());
    var users = userRepository.findAll(pageRequest);

    assertThat(users.getTotalElements()).isGreaterThan(0);
    assertThat(users.getContent().get(0).getUsername()).isEqualTo("user9");
    assertThat(users.getContent().get(0).getProfile()).isNotNull();
  }

  @Test
  @DisplayName("사용자가 없을 경우 페이징 및 정렬된 사용자 목록 조회 시 빈 값 반환")
  public void testFindUsersPagedAndSorted_Empty() {
    var pageRequest = PageRequest.of(0, 5, Sort.by("username").descending());

    var users = userRepository.findAll(pageRequest);

    assertThat(users.getTotalElements()).isEqualTo(0);
    assertThat(users.getContent()).isEmpty();
  }
}
