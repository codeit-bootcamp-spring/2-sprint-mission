package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("username으로 유저 조회 성공")
    void findByUsername_success() {
        User user = new User("user1", "user1@email.com", "1234", null);
        userRepository.save(user);

        Optional<User> result = userRepository.findByUsername("user1");

        User found = result.orElseThrow();
        assertThat(found.getEmail()).isEqualTo("user1@email.com");
    }

    @Test
    @DisplayName("존재하지 않는 username 조회 시 empty 반환")
    void findByUsername_fail() {
        Optional<User> result = userRepository.findByUsername("nonexistent");

        assertThat(result).isEmpty();
    }
}
