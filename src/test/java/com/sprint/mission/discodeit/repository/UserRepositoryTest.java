package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
   @Autowired
    private TestEntityManager entityManager;

    private User createTestUser(String username, String email) {
        BinaryContent profile = new BinaryContent("profile.jpg", 1024L, "image/jpeg");
        User user = new User(username, email, "password123!@#", profile);
        //userstatus 생성 및 연결
        UserStatus status = new UserStatus(user, Instant.now());
        return user;
    }

    @Test
    @DisplayName("사용자 이름으로 사용자를 찾을 수 있다")
    void findByUsername_ExistingUsername_ReturnsUser() {
        //given
        String username = "testUser";
        User user = createTestUser(username, "test@example.com");
        userRepository.save(user);

        //영속성 컨텍스트 초기화 - 1차 캐시 비우기
        entityManager.flush();
        entityManager.clear();

        //when
        Optional<User> foundUser = userRepository.findByUsername(username);

        //then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo(username);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 이름으로 검색하면 빈 Optional 반환")
    void findByUsername_NonExistingUsername_ReturnsEmptyOptional() {
        //given
        String nonExistingUsername = "nonExistingUsername";

        //when
        Optional<User> foundUser = userRepository.findByUsername(nonExistingUsername);

        //then
        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("이메일로 사용자 존재 여부를 확인할 수 있다.")
    void existsByEmail_ExistingEmail_ReturnsTrue() {
        //given
        String email = "test@example.com";
        User user = createTestUser("testUser", email);
        userRepository.save(user);

        //when
        boolean exists = userRepository.existsByEmail(email);

        //then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 확인하면 false 반환한다.")
    void existsByEmail_NonExistingEmail_ReturnsFalse() {
        //given
        String nonExistingEmail = "nonExisting@example.com";

        //when
        boolean exists = userRepository.existsByEmail(nonExistingEmail);

        //then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("모든 사용자를 프로필과 상태 정보와 함께 조회할 수 있다")
    void findAllWithProfileAndStatus_ReturnsUserWithProfileAndStatus() {
        //given
        User user1 = createTestUser("user1", "user1@example.com");
        User user2 = createTestUser("user2", "user2@example.com");

        userRepository.saveAll(List.of(user1, user2));

        entityManager.flush();
        entityManager.clear();

        //when
        List<User> users = userRepository.findAllWithProfileAndStatus();

        //then
        assertThat(users).hasSize(2);
        assertThat(users).extracting("username").containsExactlyInAnyOrder("user1", "user2");

        //프로필과 상태 정보가 함께 조회되었는지 확인 - 프록시 초기화 없이도 접근 가능한지 테스트
        User foundUser1 = users.stream().filter(u -> u.getUsername().equals("user1")).findFirst().orElseThrow();
        User foundUser2 = users.stream().filter(u -> u.getUsername().equals("user2")).findFirst().orElseThrow();

        //프록시 초기화 여부 확인
        assertThat(Hibernate.isInitialized(foundUser1.getProfile())).isTrue();
        assertThat(Hibernate.isInitialized(foundUser1.getStatus())).isTrue();
        assertThat(Hibernate.isInitialized(foundUser2.getProfile())).isTrue();
        assertThat(Hibernate.isInitialized(foundUser2.getStatus())).isTrue();
    }
}
