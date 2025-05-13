package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@Sql(scripts = "classpath:schema.sql")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User createUser() {
        return User.builder()
                .username("testUser")
                .email("test@example.com")
                .build();
    }

    // 성공 케이스: 사용자 이름으로 조회
    @Test
    void findByUsername_Success() {
        // Given
        User savedUser = userRepository.save(createUser());

        // When
        Optional<User> foundUser = userRepository.findByUsername("testUser");

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId());
    }

    // 실패 케이스: 존재하지 않는 사용자 이름
    @Test
    void findByUsername_NotFound() {
        // When
        Optional<User> foundUser = userRepository.findByUsername("nonExistingUser");

        // Then
        assertThat(foundUser).isEmpty();
    }

    // 성공 케이스: 이메일 존재 확인
    @Test
    void existsByEmail_True() {
        // Given
        userRepository.save(createUser());

        // When
        boolean exists = userRepository.existsByEmail("test@example.com");

        // Then
        assertThat(exists).isTrue();
    }

    // 실패 케이스: 존재하지 않는 이메일
    @Test
    void existsByEmail_False() {
        // When
        boolean exists = userRepository.existsByEmail("wrong@example.com");

        // Then
        assertThat(exists).isFalse();
    }

    // 성공 케이스: 사용자명 존재 확인
    @Test
    void existsByUsername_True() {
        // Given
        userRepository.save(createUser());

        // When
        boolean exists = userRepository.existsByUsername("testUser");

        // Then
        assertThat(exists).isTrue();
    }

    // 실패 케이스: 존재하지 않는 사용자명
    @Test
    void existsByUsername_False() {
        // When
        boolean exists = userRepository.existsByUsername("nonExistingUser");

        // Then
        assertThat(exists).isFalse();
    }
}
