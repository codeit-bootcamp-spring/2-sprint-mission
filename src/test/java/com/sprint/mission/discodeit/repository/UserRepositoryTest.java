package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;
    
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_EMAIL = "testuser@naver.com";
    private static final String TEST_PASSWORD = "password123";
    private static final String NONEXISTENT_USERNAME = "nonexistent";
    private static final String NONEXISTENT_EMAIL = "nonexistent@naver.com";
    
    
    private User createTestUser() {
        User user = new User(TEST_USERNAME, TEST_EMAIL, TEST_PASSWORD, null);
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }

    @Test
    @DisplayName("사용자명으로 사용자 찾기 - 성공")
    void findByUsername_Success() {
        createTestUser();

        var foundUser = userRepository.findByUsername(TEST_USERNAME);

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo(TEST_USERNAME);
        assertThat(foundUser.get().getEmail()).isEqualTo(TEST_EMAIL);
    }

    @Test
    @DisplayName("사용자명으로 사용자 찾기 - 실패")
    void findByUsername_NotFound() {
        var foundUser = userRepository.findByUsername(NONEXISTENT_USERNAME);

        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("사용자명 중복 확인 - 존재함")
    void existsByUsername_True() {
        createTestUser();

        boolean exists = userRepository.existsByUsername(TEST_USERNAME);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("사용자명 중복 확인 - 존재하지 않음")
    void existsByUsername_False() {
        boolean exists = userRepository.existsByUsername(NONEXISTENT_USERNAME);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("이메일 중복 확인 - 존재함")
    void existsByEmail_True() {
        createTestUser();

        boolean exists = userRepository.existsByEmail(TEST_EMAIL);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("이메일 중복 확인 - 존재하지 않음")
    void existsByEmail_False() {
        boolean exists = userRepository.existsByEmail(NONEXISTENT_EMAIL);

        assertThat(exists).isFalse();
    }
} 