package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.config.JpaAuditingConfig;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnitUtil;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@Import(JpaAuditingConfig.class)
@DataJpaTest
@ActiveProfiles("test")
public class JpaUserRepositoryTest {

  @Autowired
  UserRepository userRepository;

  @Autowired
  private EntityManager entityManager;

  private User user1;
  private User user2;
  private UserStatus userStatus1;
  private UserStatus userStatus2;
  private BinaryContent profile1;
  private BinaryContent profile2;

  @BeforeEach
  void setUp() {
    profile1 = BinaryContent.builder()
        .filename("file1")
        .contentType("img/png")
        .size(5)
        .build();

    user1 = User.builder()
        .username("test1")
        .email("test1@test.com")
        .password("1234")
        .profile(profile1)
        .build();

    userRepository.save(user1);

    userStatus1 = new UserStatus(user1, Instant.now());
    user1.updateUserStatus(userStatus1);

    profile2 = BinaryContent.builder()
        .filename("file2")
        .contentType("img/png")
        .size(5)
        .build();

    user2 = User.builder()
        .username("test2")
        .email("test2@test.com")
        .password("1234")
        .profile(profile2)
        .build();

    userRepository.save(user2);

    userStatus2 = new UserStatus(user2, Instant.now());
    user2.updateUserStatus(userStatus2);

    // BinaryContent는 UserStatus와 달리 Cascade로 관리되는 객체가 아니므로, 따로 영속성 컨텍스트에 persist
    entityManager.persist(profile1);
    entityManager.persist(profile2);
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  @DisplayName("Fetch join 모든 유저 찾기 성공")
  void fetchJoin_findAll_success() {
    // given
    PersistenceUnitUtil util = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

    // when
    List<User> users = userRepository.findAllFetch();

    // then
    // 영속성 컨텍스트에 해당 필드가 이미 로딩되어 있는지 확인 -> 로딩이 되어있다면 fetch join이 성공한 것
    for (User user : users) {
      assertThat(util.isLoaded(user, "userStatus")).isTrue();
      assertThat(util.isLoaded(user, "profile")).isTrue();
    }
    assertThat(users.size()).isEqualTo(2);
    // 영속성 컨텍스트를 clear()하고 다시 조회해서 땡겨오면, 새로운 객체 인스턴스가 생성됨
    // 객체 비교를 하면 테스트가 실패하므로, id만 비교해서 테스트!
    assertThat(users).
        extracting(User::getId)
        .containsExactlyInAnyOrder(user1.getId(), user2.getId());
  }

  @Test
  @DisplayName("UserId List에 해당하는 User 찾기 성공")
  void findAll_by_in_userIdList_success() {
    // given
    List<UUID> userIds = List.of(user1.getId(), user2.getId());

    // when
    List<User> users = userRepository.findAllByIdIn(userIds);

    // then
    assertThat(users).
        extracting(User::getId)
        .containsExactlyInAnyOrder(user1.getId(), user2.getId());
    assertThat(users).
        extracting(User::getUsername)
        .containsExactlyInAnyOrder(user1.getUsername(), user2.getUsername());
  }

  @Test
  @DisplayName("Username 중복 검사, 중복 아닐 시 false 반환")
  void username_if_not_exists_return_false() {
    // given
    String username = "newUser";

    // when
    boolean result = userRepository.existsByUsername(username);

    // then
    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("Username 중복 검사, 중복일 시 true 반환")
  void username_if_exists_return_true() {
    // given
    String username = "test1";

    // when
    boolean result = userRepository.existsByUsername(username);

    // then
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("email 중복 검사, 중복 아닐 시 flase 반환")
  void email_if_not_exists_return_false() {
    // given
    String email = "newEmail@test.com";

    // when
    boolean result = userRepository.existsByEmail(email);

    // then
    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("email 중복 검사, 중복일 시 true 반환")
  void email_if_exists_return_true() {
    // given
    String email = "test1@test.com";

    // when
    boolean result = userRepository.existsByEmail(email);

    // then
    assertThat(result).isTrue();
  }
}
