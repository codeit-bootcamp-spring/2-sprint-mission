package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("findByUsername - 존재하는 유저")
    void findByUsername_success() {
        // given
        User user = new User("tester", "test@email.com", "pw", null);
        userRepository.save(user);

        // when
        Optional<User> result = userRepository.findByUsername("tester");

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("tester");
    }

    @Test
    @DisplayName("findByUsername - 존재하지 않는 유저")
    void findByUsername_fail() {
        // when
        Optional<User> result = userRepository.findByUsername("notexist");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("existsByEmail - 존재 확인")
    void existsByEmail_success() {
        // given
        User user = new User("test2", "test2@email.com", "pw", null);
        userRepository.save(user);

        // when
        boolean exists = userRepository.existsByEmail("test2@email.com");

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsByEmail - 미존재 확인")
    void existsByEmail_fail() {
        // when
        boolean exists = userRepository.existsByEmail("no@email.com");

        // then
        assertThat(exists).isFalse();
    }
}
