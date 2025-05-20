package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.domain.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
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
  private TestEntityManager entityManager;

  @Test
  @DisplayName("findByEmail_성공")
  void findByEmail_WithExistingEmail_ShouldReturnMember() {
    User user = User.create("강아지", "bbori@dog.com", "dog123!", null);
    entityManager.persist(user);
    entityManager.flush();

    Optional<User> found = userRepository.findByEmail("bbori@dog.com");

    assertThat(found).isPresent();
    assertThat(found.get().getUsername()).isEqualTo("강아지");
  }

  @Test
  @DisplayName("findByEmail_실패_존재하지 않는 이메일")
  void findByEmail_WithNonExistingEmail_ShouldReturnEmpty() {
    Optional<User> found = userRepository.findByEmail("bbori@dog.com");

    assertThat(found).isEmpty();
  }

}
