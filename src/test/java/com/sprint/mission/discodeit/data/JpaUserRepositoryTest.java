package com.sprint.mission.discodeit.data;


import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jpa.JpaUserRepository;
import com.sprint.mission.discodeit.repository.springjpa.SpringDataUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class JpaUserRepositoryTest {

    @Autowired
    SpringDataUserRepository userRepository;

    @Test
    @DisplayName("유저 저장 및 조회 성공")
    void saveAndFindById_success() {
        // given
        User user = new User("user1", "user1@email.com", "pw", null);
        userRepository.save(user);

        // when
        Optional<User> found = userRepository.findById(user.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("user1");
    }

    @Test
    @DisplayName("존재하지 않는 ID 조회 실패")
    void findById_fail() {
        // when
        Optional<User> found = userRepository.findById(UUID.randomUUID());

        // then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("이메일 중복 체크")
    void existsByEmail_success() {
        // given
        User user = new User("user2", "user2@email.com", "pw", null);
        userRepository.save(user);

        // when
        boolean exists = userRepository.existsByEmail("user2@email.com");

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("이메일 중복 체크 실패")
    void existsByEmail_fail() {
        // when
        boolean exists = userRepository.existsByEmail("notfound@email.com");

        // then
        assertThat(exists).isFalse();
    }
}
