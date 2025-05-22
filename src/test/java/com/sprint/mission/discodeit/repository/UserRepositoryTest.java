package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private TestEntityManager em;

  @Test
  public void findByIdWithProfileAndUserStatus_shouldFetchJoinSuccessfully() {
    User user1 = createUser("user1", "user1@gmail.com");
    em.flush();
    em.clear();

    User res = userRepository.findByIdWithProfileAndUserStatus(user1.getId()).orElse(null);

    assertThat(res).isNotNull();
    assertThat(res.getProfile()).isNotNull();
    assertThat(res.getUserStatus()).isNotNull();
    assertThat(res.getProfile().getFileName()).isEqualTo("human.jpg");
  }

  @Test
  public void findAllWithProfileAndUserStatus_shouldFetchJoinSuccessfully() {
    User user1 = createUser("user1", "user1@gmail.com");
    User user2 = createUser("user2", "user2@gmail.com");
    em.flush();
    em.clear();

    List<User> res = userRepository.findAllWithProfileAndUserStatus();

    assertThat(res).hasSize(2);
    for (User u : res) {
      assertThat(u.getProfile()).isNotNull();
      assertThat(u.getUserStatus()).isNotNull();
      assertThat(u.getProfile().getFileName()).isEqualTo("human.jpg");
    }
  }

  private User createUser(String username, String email) {
    BinaryContent profile = new BinaryContent("human.jpg", "image/jpeg", 1024L);
    User user = new User(username, email, "user1234", profile);
    UserStatus status = new UserStatus(user, Instant.now());

    em.persist(profile);
    em.persist(user);
    em.persist(status);

    return user;
  }
}
