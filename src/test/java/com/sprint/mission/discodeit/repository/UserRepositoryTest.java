package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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

    private User savedUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        savedUser = new User("test", "test@gmail.com", "password", null);
        UserStatus userStatus = new UserStatus(savedUser, Instant.now());

        userRepository.save(savedUser);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("유저 존재 시 프로필과 상태도 함께 조회")
    void findAllWithProfileAndStatus_success() {
        List<User> users = userRepository.findAllWithProfileAndStatus();
        assertThat(users).isNotEmpty();
        assertThat(users).hasSize(1);

        User user = users.get(0);

        assertThat(user.getUsername()).isEqualTo(savedUser.getUsername());
        assertThat(user.getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(user.getStatus()).isNotNull();
    }

    @Test
    @DisplayName("유저가 없을 경우 findAllWithProfileAndStatus는 빈 리스트를 반환")
    void findAllWithProfileAndStatus_noUsers() {
        userRepository.deleteAll();

        List<User> users = userRepository.findAllWithProfileAndStatus();

        assertThat(users).isEmpty();
    }
}
